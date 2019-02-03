package com.scio.cloud.stream.message.receive;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;
/**
 * Enable Receiver
 *
 * @author Wang.ch
 * @date 2019-02-02 08:56:54
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ReceiverAutoConfig.class)
@Documented
public @interface EnableReceiver {}
