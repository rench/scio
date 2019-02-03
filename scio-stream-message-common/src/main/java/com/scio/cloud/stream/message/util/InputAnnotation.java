package com.scio.cloud.stream.message.util;

import java.lang.annotation.Annotation;

import org.springframework.cloud.stream.annotation.Input;
/**
 * InputAnnotation implementation store
 * @see InputAnnotation
 * @author Wang.ch
 * @date 2019-02-02 11:58:04
 */
public class InputAnnotation implements org.springframework.cloud.stream.annotation.Input {

  private String value;

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public Class<? extends Annotation> annotationType() {
    return Input.class;
  }

  @Override
  public String value() {
    return value;
  }
}
