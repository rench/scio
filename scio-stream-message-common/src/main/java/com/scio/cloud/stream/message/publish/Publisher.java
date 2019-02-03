package com.scio.cloud.stream.message.publish;
/**
 * Publisher interface
 *
 * @author Wang.ch
 * @date 2019-02-02 08:53:42
 */
public interface Publisher<T> {

  /**
   * @param message the message to be sent
   * @return whether or not the message was sent
   */
  boolean sendMessage(T message);

  /**
   * Send a message, blocking until either the message is accepted or thespecified timeout period
   * elapses.
   *
   * @param message
   * @param timeout the timeout in milliseconds
   * @return {@code true} if the message is sent, {@code false} if not including a timeout of an
   *     interrupt of the send
   */
  boolean sendMessage(T message, long timeout);
}
