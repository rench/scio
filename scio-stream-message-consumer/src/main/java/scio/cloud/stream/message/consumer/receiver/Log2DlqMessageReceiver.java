package scio.cloud.stream.message.consumer.receiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;
import com.scio.cloud.stream.message.model.StreamMessage;
import com.scio.cloud.stream.message.receive.Receiver;

import scio.cloud.stream.message.consumer.receiver.config.Log2MessageBinder;
/**
 * log dead letter queue receiver
 *
 * @author Wang.ch
 * @date 2019-03-11 15:39:46
 */
@Component
public class Log2DlqMessageReceiver implements Receiver<StreamMessage> {
  private static final Logger LOG = LoggerFactory.getLogger(Log2DlqMessageReceiver.class);

  @Override
  @StreamListener(Log2MessageBinder.INPUT_DLQ)
  public void onMessage(Message<StreamMessage> message) {
    LOG.info(
        "log2 dlq message receiver receive a message @ thread:{}",
        Thread.currentThread().getName());
    Channel channel = message.getHeaders().get(AmqpHeaders.CHANNEL, Channel.class);
    Long deliveryTag = message.getHeaders().get(AmqpHeaders.DELIVERY_TAG, Long.class);
    // always send ack
    try {
      channel.basicAck(deliveryTag, false);
    } catch (Exception e) {
      LOG.error("channle basicAck exception", e);
    }
  }
}
