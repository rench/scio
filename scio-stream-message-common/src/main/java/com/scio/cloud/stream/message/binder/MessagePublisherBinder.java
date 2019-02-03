package com.scio.cloud.stream.message.binder;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
/**
 * Message publisher binder
 *
 * @author wang.ch
 * @date 2019-02-02 08:20:45
 */
public interface MessagePublisherBinder {

  /**
   * Message output channel
   *
   * @return
   */
  @Output("${scio.cloud.stream.message.publisher.channel}")
  public MessageChannel publish();
}
