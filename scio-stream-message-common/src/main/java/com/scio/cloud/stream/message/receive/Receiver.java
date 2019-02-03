package com.scio.cloud.stream.message.receive;

import org.springframework.messaging.Message;

public interface Receiver<T> {

  void onMessage(Message<T> message);
}
