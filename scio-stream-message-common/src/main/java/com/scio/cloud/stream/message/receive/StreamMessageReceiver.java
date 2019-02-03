package com.scio.cloud.stream.message.receive;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.support.MessageBuilder;

import com.rabbitmq.client.Channel;
import com.scio.cloud.stream.message.binder.MessageReceiverBinder;
import com.scio.cloud.stream.message.model.StreamMessage;
import com.scio.cloud.stream.message.process.AbstractMessageProcessor;
/**
 * Stream Message Receiver
 *
 * @author Wang.ch
 * @date 2019-02-02 08:54:36
 */
public class StreamMessageReceiver
    implements Receiver<StreamMessage>, ApplicationContextAware, InitializingBean {
  private static final Logger LOG = LoggerFactory.getLogger(StreamMessageReceiver.class);
  /** for json */
  private MappingJackson2MessageConverter converter;
  /** spring application context */
  private ApplicationContext ctx;
  /** message senders */
  private Map<String, AbstractMessageProcessor> senders;
  /** stream message receiver binder */
  private MessageReceiverBinder binder;

  public StreamMessageReceiver(MessageReceiverBinder binder) {
    super();
    this.binder = binder;
    this.converter = new MappingJackson2MessageConverter();
    this.subscribe();
  }

  public void setBinder(MessageReceiverBinder binder) {
    this.binder = binder;
  }

  @SuppressWarnings("unchecked")
  private void subscribe() {
    binder
        .receive()
        .subscribe(
            m -> {
              StreamMessage converted =
                  (StreamMessage) converter.fromMessage(m, StreamMessage.class);
              Message<StreamMessage> streamMessage;
              if (converted == null) {
                streamMessage = (Message<StreamMessage>) m;
              } else {
                streamMessage = MessageBuilder.createMessage(converted, m.getHeaders());
              }
              onMessage(streamMessage);
            });
  }

  @Override
  public void onMessage(Message<StreamMessage> message) {
    AtomicInteger count = new AtomicInteger(senders.entrySet().size());
    senders
        .entrySet()
        .stream()
        .forEach(
            entry -> {
              StreamMessage msg = message.getPayload();
              AbstractMessageProcessor sender = entry.getValue();
              if (sender.shouldProcess(msg)) {
                try {
                  sender.onMessage(message);
                  count.decrementAndGet();
                } catch (Throwable e) {
                  LOG.error("sender onMessage exception", e);
                }
              }
            });
    Channel channel = message.getHeaders().get(AmqpHeaders.CHANNEL, Channel.class);
    Long deliveryTag = message.getHeaders().get(AmqpHeaders.DELIVERY_TAG, Long.class);
    // always send ack
    try {
      channel.basicAck(deliveryTag, false);
    } catch (Exception e) {
      LOG.error("channle basicAck exception", e);
    }
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.ctx = applicationContext;
    this.senders = this.ctx.getBeansOfType(AbstractMessageProcessor.class);
  }

  @Override
  public void afterPropertiesSet() throws Exception {}
}
