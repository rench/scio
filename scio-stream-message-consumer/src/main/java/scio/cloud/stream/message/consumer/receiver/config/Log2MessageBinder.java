package scio.cloud.stream.message.consumer.receiver.config;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface Log2MessageBinder {
  public static final String INPUT = "log2";

  @Input(INPUT)
  public SubscribableChannel receive();
}
