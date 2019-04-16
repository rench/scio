# 通过RequestBodyAdvice对@RequestBody属性进行统一处理

## scio
> https://github.com/rench/scio/tree/master/scio

[![CircleCI](https://circleci.com/gh/rench/scio.svg?style=svg)](https://circleci.com/gh/rench/scio)
[![Auth](https://img.shields.io/badge/Author-Wang.ch-blue.svg)](https://xuankejia.cn)
[![GitHub stars](https://img.shields.io/github/stars/rench/scio.svg?style=social&label=Stars)](https://github.com/rench/scio)
[![GitHub forks](https://img.shields.io/github/forks/rench/scio.svg?style=social&label=Fork)](https://github.com/rench/scio)

------

在平时的开发中，我们定义RestController的时候，会通过@RequestBody进行JSON参数接受，并且会有一个通用的BaseRequest进行统一封装，BaseRequest的属性一般是由请求方传入，有些特殊场景需要针对统一的请求参数进行特殊处理，比如拦截、预处理、日志记录等：

> * SpringMvc是如何把JSON请求转换为JavaBean?
> * 如何在调用Controller的方法前进行JavaBean的处理?
> * 相关资料

------

## SpringMvc是如何把JSON请求转换为JavaBean?

我们在定义Controller方法的时候，可以指定一个JavaBean作为参数，SpringMvc会自动帮我们生成并填充JavaBean，属性值来源于**url-parameter**或者**form-body**。在JavaBean前添加**@RequestBody**注解，SpringMvc会自动把JSON转换为JavaBean。
### 1. 如何寻找切入点

- **@RequestBody**，查看其定义，里面有`boolean required() default true;`，利用IDE查看required方法的引用，Eclipse下双击选择方法，Ctrl+Alt+H
- **RequestResponseBodyMethodProcessor**，该类调用了`required()`方法进行判断，再根据类名初步推断该类就是请求和响应的处理器。
- **RequestResponseBodyMethodProcessor::readWithMessageConverters**，该方法会根据请求的类型和参数，调用不同的消息转换器来进行消息处理，查看调用`required()`方法前,调用了父类方法。
- **AbstractMessageConverterMethodArgumentResolver::readWithMessageConverters**，该方法会根据**content-type**，同时遍历**messageConverters**，进行消息的转换。继续向下跟进发现下面代码：
```
if (message.hasBody()) {
  //转换前处理
  HttpInputMessage msgToUse =
    getAdvice().beforeBodyRead(message, parameter, targetType, converterType);
  body = (genericConverter != null ? genericConverter.read(targetType, contextClass, msgToUse) :
            ((HttpMessageConverter<T>) converter).read(targetClass, msgToUse));
  //转换后处理
  body = getAdvice().afterBodyRead(body, msgToUse, parameter, targetType, converterType);
}
```
> 在遍历消息转换器进行消息处理的时候：转换前，转换后，会调用**RequestResponseBodyAdviceChain**对消息进行两次处理。而根据**RequestResponseBodyAdviceChain**名称可知，这是一个advice链，处理的次数可能是多次。由此可以发现，我们在消息转换前，转换后都可以对消息进行加工处理，由此可见Aop思想在切面编程上的优势。

- **RequestResponseBodyAdviceChain**，继续跟进，`getAdvice()`返回的是成员变量**advice**，它的设置是通过构造函数设置，继续调用`Ctrl+Alt+H`
- **AbstractMessageConverterMethodArgumentResolver**
```
public AbstractMessageConverterMethodArgumentResolver(List<HttpMessageConverter<?>> converters,
        @Nullable List<Object> requestResponseBodyAdvice) {
    Assert.notEmpty(converters, "'messageConverters' must not be empty");
    this.messageConverters = converters;
    this.allSupportedMediaTypes = getAllSupportedMediaTypes(converters);
    //把advice列表构建成链
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
> 继续跟进，**requestResponseBodyAdvice**成员变量的来源，除了setter方法以外，在**private void initControllerAdviceCache()**方法中
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
> 通过以上代码，可以发现，注册逻辑是通过在`applicationContext`中寻找**@ControllerAdvice**注解的类，然后继续针对扫描处理的实例，判断：
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
### 2. 如何在调用Controller的方法前进行JavaBean的处理?
通过上面的跟踪发现，我们只需要新建一个类，并实现**RequestBodyAdvice**接口，同时在类上添加**@ControllerAdvice**注解，就可以被SpringMvc扫描并自动加载。

那么这个实现类应该怎么写？老方法，查看**RequestBodyAdvice**接口的实现类即可，发现有个**RequestBodyAdviceAdapter**抽象类实现了该接口，这个Adapter只是默认的实现，没有对消息进行处理，我们自定义实现了，可以直接继承**RequestBodyAdviceAdapter**，根据需要覆盖自己需要处理的方法即可，这也是**Adapter**设计模式的用处之一。
以下是针对一个简单的实现，包括了对JavaBean属性覆盖，已经通用Session的操作。
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

### 3. 相关资料
- [scio-jwt](https://github.com/rench/scio/tree/master/scio-cloud-jwt)
- [假装看源码之springmvc (一) 如何进行请求参数的解析](https://blog.csdn.net/shuixiou1/article/details/79676859)