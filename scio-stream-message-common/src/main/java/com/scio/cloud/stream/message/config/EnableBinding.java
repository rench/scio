package com.scio.cloud.stream.message.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.config.BinderFactoryConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.config.EnableIntegration;

/**
 * Enables the binding of targets annotated with {@link Input} and {@link Output} to a broker,
 * according to the list of interfaces passed as value to the annotation.
 *
 * @author Dave Syer
 * @author Marius Bogoevici
 * @author David Turanski
 * @author Wang.ch
 */
@SuppressWarnings("deprecation")
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Configuration
@Import({BindingBeansRegistrar.class, BinderFactoryConfiguration.class})
@EnableIntegration
public @interface EnableBinding {

  /**
   * A list of interfaces having methods annotated with {@link Input} and/or {@link Output} to
   * indicate binding targets.
   */
  Class<?>[] value() default {};
}
