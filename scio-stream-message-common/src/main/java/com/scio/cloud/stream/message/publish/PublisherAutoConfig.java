package com.scio.cloud.stream.message.publish;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.Assert;

import com.scio.cloud.stream.message.binder.MessagePublisherBinder;

/**
 * Publisher Auto config
 *
 * @author Wang.ch
 * @date 2019-02-01 10:57:34
 */
@Import(MessagePublisherBinderConfig.class)
public class PublisherAutoConfig {
  private static final Logger LOG = LoggerFactory.getLogger(PublisherAutoConfig.class);

  @Value("${scio.cloud.stream.message.publisher.enabled:}")
  private String enabled;

  @Value("${scio.cloud.stream.message.publisher.channel:}")
  private String publisherChannel;

  @PostConstruct
  protected void init() {
    Assert.hasText(
        enabled,
        "please check the config and set scio.cloud.stream.message.publisher.enabled value ");
    Assert.hasText(
        publisherChannel,
        "please check the config and set scio.cloud.stream.message.publisher.channel value ");
  }

  @Bean("streamMessagePublisher")
  @ConditionalOnProperty(
      prefix = "scio.cloud.stream.message.publisher",
      name = "enabled",
      havingValue = "true")
  public static StreamMessagePublisher publisher(@Lazy MessagePublisherBinder binder) {
    LOG.info("Initializing streamMessagePublisher");
    return new StreamMessagePublisher(binder);
  }
}
