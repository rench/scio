package scio.cloud.stream.message.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scio.cloud.stream.message.model.StreamMessage;
import com.scio.cloud.stream.message.model.StreamMessage.MessageFrom;
import com.scio.cloud.stream.message.model.StreamMessage.MessageType;
import com.scio.cloud.stream.message.model.StreamMessage.SendSource;
import com.scio.cloud.stream.message.publish.EnablePublisher;
import com.scio.cloud.stream.message.publish.Publisher;
/**
 * Message Producer Application with publish api
 *
 * @author Wang.ch
 * @date 2019-03-11 15:41:21
 */
@SpringBootApplication
@EnablePublisher
public class ScioMessageProducerApplication {
  public static void main(String[] args) {
    SpringApplication.run(ScioMessageProducerApplication.class, args);
  }

  @RestController
  public static class PublishController {
    @Autowired @Lazy private Publisher<StreamMessage> publisher;

    @RequestMapping("/pub")
    public String publish() {
      StreamMessage msg =
          new StreamMessage.Builder()
              .id(1L)
              .from(MessageFrom.STORE)
              .template("test message")
              .sendSources(SendSource.SMS)
              .type(MessageType.ORDER)
              .build();
      publisher.sendMessage(msg);
      return "ok";
    }
  }
}
