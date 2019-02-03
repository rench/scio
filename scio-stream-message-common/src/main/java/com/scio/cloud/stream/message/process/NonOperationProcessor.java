package com.scio.cloud.stream.message.process;

import com.scio.cloud.stream.message.model.StreamMessage;
/**
 * Default non operation processor
 *
 * @author Wang.ch
 * @date 2019-02-02 09:24:52
 */
public class NonOperationProcessor extends AbstractMessageProcessor {

  @Override
  public boolean shouldProcess(StreamMessage message) {
    return false;
  }

  @Override
  public void process(StreamMessage message) {}
}
