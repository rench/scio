package scio.cloud.stream.message.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.scio.cloud.stream.message.receive.EnableReceiver;
/**
 * Message consumer Application
 * @author Wang.ch
 * @date 2019-03-11 15:41:00
 */
@SpringBootApplication
@EnableReceiver
public class ScioMessageConsumerApplication {
  public static void main(String[] args) {
    SpringApplication.run(ScioMessageConsumerApplication.class, args);
  }
}
