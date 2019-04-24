package com.scio.cloud.jooq.utils;

import java.util.function.Supplier;

import org.springframework.beans.BeanUtils;
/**
 * bean拷贝工具类
 *
 * @author Wang.ch
 * @date 2019-03-01 09:14:51
 */
public class BeanCopyUtils {
  /**
   * 对象拷贝方法,使用 org.springframework.beans.BeanUtils.copyProperties
   *
   * @param s 源对象
   * @param supplier 目标对象实例提供者
   * @return
   */
  public static <S, D> D copy(S s, Supplier<D> supplier) {
    D d = supplier.get();
    if (null != s && null != d) {
      BeanUtils.copyProperties(s, d);
      return d;
    }
    return null;
  }
  /**
   * 对象拷贝方法,使用 org.springframework.beans.BeanUtils.copyProperties
   *
   * @param s 源对象
   * @param d 目标对象
   * @return
   */
  public static <S, D> D copy(S s, D d) {
    if (null != s && null != d) {
      BeanUtils.copyProperties(s, d);
      return d;
    }
    return null;
  }
  /**
   * 对象拷贝
   *
   * @param s 源对象
   * @param d 目标对象
   * @param copy 拷贝接口方法
   * @return
   */
  public static <S, D> D copy(S s, D d, CopyFunction<S, D> copy) {
    if (null != s && null != d) {
      copy.copy(s, d);
      return d;
    }
    return null;
  }
  /**
   * 拷贝方法
   *
   * @author Wang.ch
   * @date 2019-03-01 09:18:43
   * @param <S>
   * @param <D>
   */
  public interface CopyFunction<S, D> {
    /**
     * 拷贝方法
     *
     * @param s
     * @param d
     */
    void copy(S s, D d);
  }
}
