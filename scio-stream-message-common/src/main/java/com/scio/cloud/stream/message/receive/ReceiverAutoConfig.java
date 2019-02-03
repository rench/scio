package com.scio.cloud.stream.message.receive;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.Assert;

import com.scio.cloud.stream.message.binder.MessageReceiverBinder;
import com.scio.cloud.stream.message.config.EnableBinding;
/**
 * Message Receiver Autoconfig
 *
 * @author Wang.ch
 * @date 2019-02-02 08:33:08
 */
@Import(ReceiverAutoConfig.MessageStreamChannelConfig.class)
public class ReceiverAutoConfig {
  private static final Logger LOG = LoggerFactory.getLogger(ReceiverAutoConfig.class);

  @Value("${scio.cloud.stream.message.receiver.enabled:}")
  private String enabled;

  @Value("${scio.cloud.stream.message.receiver.channel:}")
  private String receiverChannel;

  @PostConstruct
  protected void init() {
    Assert.hasText(
        enabled,
        "please check the application config and set scio.cloud.stream.message.receiver.enabled value");
    Assert.hasText(
        receiverChannel,
        "please check the application config and set scio.cloud.stream.message.receiver.channel value");
  }

  @Bean("streamMessageReceiver")
  @ConditionalOnProperty(
      prefix = "scio.cloud.stream.message.receiver",
      name = "enabled",
      havingValue = "true")
  protected StreamMessageReceiver publisher(MessageReceiverBinder binder) {
    LOG.info("Initializing streamMessageReceiver ");
    return new StreamMessageReceiver(binder);
  }

  @Configuration
  @ConditionalOnProperty(
      prefix = "scio.cloud.stream.message.receiver",
      name = "enabled",
      havingValue = "true")
  @EnableBinding(MessageReceiverBinder.class)
  public class MessageStreamChannelConfig {}
}
