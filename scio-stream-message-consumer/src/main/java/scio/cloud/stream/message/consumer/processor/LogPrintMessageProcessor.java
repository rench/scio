package scio.cloud.stream.message.consumer.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.scio.cloud.stream.message.model.StreamMessage;
import com.scio.cloud.stream.message.process.AbstractMessageProcessor;

@Component("logPrintMessageProcessor")
public class LogPrintMessageProcessor extends AbstractMessageProcessor {
  private static final Logger LOG = LoggerFactory.getLogger(LogPrintMessageProcessor.class);

  @Override
  public boolean shouldProcess(StreamMessage message) {
    return true;
  }

  @Override
  public void process(StreamMessage message) {
    LOG.info(
        "LogPrintMessageProcessor receive message @ thread :{}", Thread.currentThread().getName());
  }
}
