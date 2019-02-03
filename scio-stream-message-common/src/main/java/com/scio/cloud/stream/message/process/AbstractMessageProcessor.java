package com.scio.cloud.stream.message.process;

import org.springframework.messaging.Message;

import com.scio.cloud.stream.message.model.StreamMessage;

/**
 * Abstract message processor <br>
 * Note that, default receiver will multcast the message payload to the sender extends
 * AbstractMessageProcessor <br>
 * Default Stream Message Receiver will ignore any exception when invoke sender,and always ack the
 * message <br>
 * If your want ack/nack the message own,you should custom implementation your Receiver
 * with @StreamListener on onMessage method.
 *
 * @author Wang.ch
 * @date 2019-02-02 09:04:13
 */
public abstract class AbstractMessageProcessor {
  /**
   * Verify that the message can be sent through this processor
   *
   * @param message
   * @return
   */
  public abstract boolean shouldProcess(StreamMessage message);

  /**
   * do process business
   *
   * @param message
   */
  public abstract void process(StreamMessage message);

  /**
   * on Message event
   *
   * @param message
   */
  public void onMessage(Message<StreamMessage> message) {
    process(message.getPayload());
  }
}
