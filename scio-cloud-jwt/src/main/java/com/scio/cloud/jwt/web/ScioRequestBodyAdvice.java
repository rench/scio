package com.scio.cloud.jwt.web;

import java.lang.reflect.Type;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import com.scio.cloud.jwt.web.vo.QueryVo;
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
