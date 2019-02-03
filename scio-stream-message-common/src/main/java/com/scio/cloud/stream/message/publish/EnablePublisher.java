package com.scio.cloud.stream.message.publish;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

/**
 * enable message publisher
 *
 * @author Wang.ch
 * @date 2019-02-01 10:52:29
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(PublisherAutoConfig.class)
@Documented
public @interface EnablePublisher {}
