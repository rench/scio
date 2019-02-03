package com.scio.cloud.stream.message.binder;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;
/**
 * Message receiver stream binder
 *
 * @author wang.ch
 * @date 2019-02-02 08:21:00
 */
public interface MessageReceiverBinder {

  /**
   * stream input channel
   *
   * @return
   */
  @Input("${scio.cloud.stream.message.receiver.channel}")
  public SubscribableChannel receive();
}
