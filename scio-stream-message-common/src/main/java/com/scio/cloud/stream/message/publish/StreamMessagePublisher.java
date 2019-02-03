package com.scio.cloud.stream.message.publish;

import org.springframework.messaging.support.MessageBuilder;

import com.scio.cloud.stream.message.binder.MessagePublisherBinder;
import com.scio.cloud.stream.message.model.StreamMessage;
/**
 * Stream Message Publisher Service
 *
 * @author Wang.ch
 * @date 2019-02-02 08:45:31
 */
public class StreamMessagePublisher implements Publisher<StreamMessage> {
  private MessagePublisherBinder binder;

  public StreamMessagePublisher(MessagePublisherBinder binder) {
    super();
    this.binder = binder;
  }

  /** @param binder the binder to set */
  public void setBinder(MessagePublisherBinder binder) {
    this.binder = binder;
  }

  @Override
  public boolean sendMessage(StreamMessage message) {
    return this.binder.publish().send(MessageBuilder.withPayload(message).build());
  }

  @Override
  public boolean sendMessage(StreamMessage message, long timeout) {
    return this.binder.publish().send(MessageBuilder.withPayload(message).build(), timeout);
  }
}
