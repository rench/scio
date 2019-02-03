package com.scio.cloud.stream.message.publish;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import com.scio.cloud.stream.message.binder.MessagePublisherBinder;
import com.scio.cloud.stream.message.config.EnableBinding;

@Configuration
@ConditionalOnProperty(
    prefix = "scio.cloud.stream.message.publisher",
    name = "enabled",
    havingValue = "true")
@EnableBinding(MessagePublisherBinder.class)
public class MessagePublisherBinderConfig {}
