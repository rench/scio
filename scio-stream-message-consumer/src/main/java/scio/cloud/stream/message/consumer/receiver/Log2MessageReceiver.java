package scio.cloud.stream.message.consumer.receiver;

import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;
import com.scio.cloud.stream.message.config.EnableBinding;
import com.scio.cloud.stream.message.model.StreamMessage;
import com.scio.cloud.stream.message.receive.Receiver;

import scio.cloud.stream.message.consumer.receiver.config.Log2MessageBinder;
/**
 * customer receiver
 *
 * @author Wang.ch
 * @date 2019-03-11 15:40:17
 */
@Component
public class Log2MessageReceiver implements Receiver<StreamMessage> {
  private static final Logger LOG = LoggerFactory.getLogger(Log2MessageReceiver.class);
  private AtomicLong atomicLong = new AtomicLong(1);

  @Override
  @StreamListener(Log2MessageBinder.INPUT)
  public void onMessage(Message<StreamMessage> message) {
    LOG.info(
        "log2 message receiver receive a message @ thread:{}", Thread.currentThread().getName());
    Channel channel = message.getHeaders().get(AmqpHeaders.CHANNEL, Channel.class);
    Long deliveryTag = message.getHeaders().get(AmqpHeaders.DELIVERY_TAG, Long.class);
    // always send ack
    try {
      // channel.basicAck(deliveryTag, false);
      if (atomicLong.incrementAndGet() % 3 == 0) {
        // test for DLQ
        LOG.info("basic nack");
        channel.basicNack(deliveryTag, false, false);
      } else {
        channel.basicAck(deliveryTag, false);
      }
    } catch (Exception e) {
      LOG.error("channle basicAck exception", e);
    }
    // int i = 1 / 0;
  }

  @Configurable
  @EnableBinding(Log2MessageBinder.class)
  public class Log2MessageBinderConfig {}
}
