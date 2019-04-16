# Unified processing of the @RequestBody attribute via RequestBodyAdvice [中文](https://github.com/rench/scio/tree/master/scio-cloud-jwt/README-RequestBodyAdvice_zh.md)

## scio
> https://github.com/rench/scio/tree/master/scio

[![CircleCI](https://circleci.com/gh/rench/scio.svg?style=svg)](https://circleci.com/gh/rench/scio)
[![Auth](https://img.shields.io/badge/Author-Wang.ch-blue.svg)](https://xuankejia.cn)
[![GitHub stars](https://img.shields.io/github/stars/rench/scio.svg?style=social&label=Stars)](https://github.com/rench/scio)
[![GitHub forks](https://img.shields.io/github/forks/rench/scio.svg?style=social&label=Fork)](https://github.com/rench/scio)

------

In the usual development, when we define RestController, we will accept the JSON parameter through @RequestBody, and there will be a general BaseRequest for unified encapsulation. The properties of BaseRequest are generally passed in by the requester. Some special scenarios need to be unified. Request parameters for special handling, such as interception, preprocessing, logging, etc.:

> * How does SpringMvc convert JSON requests to JavaBeans?
> * How to handle JavaBeans before calling the Controller method?
> * Related Information

------

## How does SpringMvc convert JSON requests to JavaBeans?

When we define the Controller method, we can specify a JavaBean as a parameter. SpringMvc will automatically generate and populate the JavaBean for us. The property value comes from **url-parameter** or **form-body**. Add the **@RequestBody** annotation before the JavaBean, SpringMvc will automatically convert the JSON to a JavaBean.

### 1. How to find the entry point

- **@RequestBody**, view its definition, which has `boolean required() default true;`, use the IDE to view the reference to the required method, double-click the selection method under Eclipse, Ctrl+Alt+H
- **RequestResponseBodyMethodProcessor**, which calls the `required()` method to determine, and then infers that the class is the request and response handler based on the class name.
- **RequestResponseBodyMethodProcessor::readWithMessageConverters**, which calls different message converters for message processing based on the type and parameters of the request. See the parent class method before calling the `required()` method.
- **AbstractMessageConverterMethodArgumentResolver::readWithMessageConverters**, which converts messages based on **content-type** and traversing **messageConverters**. Continue to follow up and find the following code:

```
if (message.hasBody()) {
  //beforeBodyRead
  HttpInputMessage msgToUse =
    getAdvice().beforeBodyRead(message, parameter, targetType, converterType);
  body = (genericConverter != null ? genericConverter.read(targetType, contextClass, msgToUse) :
            ((HttpMessageConverter<T>) converter).read(targetClass, msgToUse));
  //afterBodyRead
  body = getAdvice().afterBodyRead(body, msgToUse, parameter, targetType, converterType);
}
```
> When traversing the message converter for message processing: Before the conversion, after the conversion, the message will be processed twice by calling **RequestResponseBodyAdviceChain**. According to the name of **RequestResponseBodyAdviceChain**, this is an advice chain, and the number of times of processing may be multiple. It can be found that we can process the message before the message is converted, so we can see the advantage of Aop thinking in aspect programming.

- **RequestResponseBodyAdviceChain**, continue to follow, `getAdvice()` returns the member variable **advice**, its setting is set by the constructor, continue to call `Ctrl+Alt+H`
- **AbstractMessageConverterMethodArgumentResolver**
```
public AbstractMessageConverterMethodArgumentResolver(List<HttpMessageConverter<?>> converters,
        @Nullable List<Object> requestResponseBodyAdvice) {
    Assert.notEmpty(converters, "'messageConverters' must not be empty");
    this.messageConverters = converters;
    this.allSupportedMediaTypes = getAllSupportedMediaTypes(converters);
    //Build the advice list into a chain
    this.advice = new RequestResponseBodyAdviceChain(requestResponseBodyAdvice);
}
```
- **AbstractMessageConverterMethodProcessor**
```
protected AbstractMessageConverterMethodProcessor(List<HttpMessageConverter<?>> converters,
            @Nullable ContentNegotiationManager manager, @Nullable List<Object> requestResponseBodyAdvice) {
    super(converters, requestResponseBodyAdvice);
    this.contentNegotiationManager = (manager != null ? manager : new ContentNegotiationManager());
    this.pathStrategy = initPathStrategy(this.contentNegotiationManager);
    this.safeExtensions.addAll(this.contentNegotiationManager.getAllFileExtensions());
    this.safeExtensions.addAll(WHITELISTED_EXTENSIONS);
}

----

public RequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> converters,
        @Nullable List<Object> requestResponseBodyAdvice) {
    super(converters, null, requestResponseBodyAdvice);
}
```
- **RequestMappingHandlerAdapter**
```
private List<HandlerMethodArgumentResolver> getDefaultArgumentResolvers() {
        List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();
        // Annotation-based argument resolution
        resolvers.add(new RequestParamMethodArgumentResolver(getBeanFactory(), false));
        resolvers.add(new RequestParamMapMethodArgumentResolver());
        resolvers.add(new PathVariableMethodArgumentResolver());
        resolvers.add(new PathVariableMapMethodArgumentResolver());
        resolvers.add(new MatrixVariableMethodArgumentResolver());
        resolvers.add(new MatrixVariableMapMethodArgumentResolver());
        resolvers.add(new ServletModelAttributeMethodProcessor(false));
        //构造方法
        resolvers.add(new RequestResponseBodyMethodProcessor(getMessageConverters(), this.requestResponseBodyAdvice));
        resolvers.add(new RequestPartMethodArgumentResolver(getMessageConverters(), this.requestResponseBodyAdvice));
```
> Continue to follow up, **requestResponseBodyAdvice** member variable source, in addition to the setter method, in the **private void initControllerAdviceCache()** method

```
private void initControllerAdviceCache() {
    if (getApplicationContext() == null) {
        return;
    }
    List<ControllerAdviceBean> adviceBeans = ControllerAdviceBean.findAnnotatedBeans(getApplicationContext());
    AnnotationAwareOrderComparator.sort(adviceBeans);
    ....
}
```
- **ControllerAdviceBean::findAnnotatedBeans**
```
public static List<ControllerAdviceBean> findAnnotatedBeans(ApplicationContext context) {
    return Arrays.stream(BeanFactoryUtils.beanNamesForTypeIncludingAncestors(context, Object.class))
            .filter(name -> context.findAnnotationOnBean(name, ControllerAdvice.class) != null)
            .map(name -> new ControllerAdviceBean(name, context))
            .collect(Collectors.toList());
}
```
> Through the above code, you can find that the registration logic is to find the class annotated by **@ControllerAdvice** in `applicationContext`, and then continue to evaluate the instance of the scan, and determine:
```
...
if (RequestBodyAdvice.class.isAssignableFrom(beanType)) {
    requestResponseBodyAdviceBeans.add(adviceBean);
}
if (ResponseBodyAdvice.class.isAssignableFrom(beanType)) {
    requestResponseBodyAdviceBeans.add(adviceBean);
}
if (!requestResponseBodyAdviceBeans.isEmpty()) {
    this.requestResponseBodyAdvice.addAll(0, requestResponseBodyAdviceBeans);
}
...
```
### 2. How do I process JavaBeans before calling the Controller method?
Through the above tracking, we only need to create a new class, and implement the **RequestBodyAdvice** interface, and add the **@ControllerAdvice** annotation to the class, which can be scanned and loaded automatically by SpringMvc.

So how should this implementation class be written? The old method, look at the implementation class of the **RequestBodyAdvice** interface, found that there is a **RequestBodyAdviceAdapter** abstract class to achieve the interface, this Adapter is only the default implementation, no processing of the message, we customize the implementation, You can directly inherit **RequestBodyAdviceAdapter** and override the methods you need to handle as needed. This is one of the uses of the **Adapter** design pattern.
The following is for a simple implementation, including the coverage of JavaBean properties, the operation of the general Session.
> https://github.com/rench/scio/blob/master/scio-cloud-jwt/src/main/java/com/scio/cloud/jwt/web/ScioRequestBodyAdvice.java
```
/**
 * advice request body java bean
 *
 * @author Wang.ch
 * @date 2019-03-28 17:15:52
 */
@ControllerAdvice
public class ScioRequestBodyAdvice extends RequestBodyAdviceAdapter {

  @Override
  public boolean supports(
      MethodParameter methodParameter,
      Type targetType,
      Class<? extends HttpMessageConverter<?>> converterType) {
    return targetType.getTypeName().equals(QueryVo.class.getName());
  }

  @Override
  public Object afterBodyRead(
      Object body,
      HttpInputMessage inputMessage,
      MethodParameter parameter,
      Type targetType,
      Class<? extends HttpMessageConverter<?>> converterType) {
    if (body != null) {
      QueryVo vo = (QueryVo) body;
      vo.setName("Advice By ScioRequestBodyAdvice");
      HttpSession session = getRequest().getSession(true);
      session.setAttribute("advice", vo);
      return vo;
    } else {
      return body;
    }
  }

  /**
   * try to get httpServletRequest from current thread holder
   *
   * @return
   */
  public static HttpServletRequest getRequest() {
    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
    HttpServletRequest request =
        (HttpServletRequest)
            requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
    return request;
  }
}
```

### 3. Relevant information
- [scio-jwt](https://github.com/rench/scio/tree/master/scio-cloud-jwt)
- [Pretend to see the source code springmvc (a) how to parse the request parameters(zh_cn)](https://blog.csdn.net/shuixiou1/article/details/79676859)