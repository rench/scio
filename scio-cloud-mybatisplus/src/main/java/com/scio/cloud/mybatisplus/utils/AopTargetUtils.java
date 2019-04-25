package com.scio.cloud.mybatisplus.utils;

import java.lang.reflect.Field;

import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;
/**
 * 代理对象获取工具
 *
 * @author wang.ch
 * @date 2019-01-23 08:46:40
 */
public class AopTargetUtils {
  /**
   * 获取 目标对象
   *
   * @param proxy 代理对象
   * @return 目标对象
   * @throws Exception
   */
  public static <T> T getTarget(T proxy) throws Exception {
    if (!AopUtils.isAopProxy(proxy)) {
      return proxy;
    }
    if (AopUtils.isJdkDynamicProxy(proxy)) {
      proxy = getJdkDynamicProxyTargetObject(proxy);
    } else {
      proxy = getCglibProxyTargetObject(proxy);
    }
    return getTarget(proxy);
  }

  @SuppressWarnings("unchecked")
  private static <T> T getCglibProxyTargetObject(T proxy) throws Exception {
    Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
    h.setAccessible(true);
    Object dynamicAdvisedInterceptor = h.get(proxy);
    Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
    advised.setAccessible(true);
    T target =
        (T) ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
    return target;
  }

  @SuppressWarnings("unchecked")
  private static <T> T getJdkDynamicProxyTargetObject(T proxy) throws Exception {
    Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
    h.setAccessible(true);
    AopProxy aopProxy = (AopProxy) h.get(proxy);
    Field advised = aopProxy.getClass().getDeclaredField("advised");
    advised.setAccessible(true);
    T target = (T) ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();
    return target;
  }
}
