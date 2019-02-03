package com.scio.cloud.stream.message.util;

import java.lang.annotation.Annotation;

import org.springframework.cloud.stream.annotation.Output;
/**
 * OutputAnnotation implementation store
 * @author Wang.ch
 * @date 2019-02-03 08:09:20
 */
public class OutputAnnotation implements org.springframework.cloud.stream.annotation.Output {

  private String value;

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public Class<? extends Annotation> annotationType() {
    return Output.class;
  }

  @Override
  public String value() {
    return value;
  }
}
