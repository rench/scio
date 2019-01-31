package com.scio.cloud.sentinel;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * scio-cloud-sentinel
 *
 * <pre>
 * Document: https://github.com/spring-cloud-incubator/spring-cloud-alibaba/blob/master/spring-cloud-alibaba-examples/sentinel-example/sentinel-core-example/readme.md
 * Document: https://github.com/alibaba/Sentinel
 * Dependencies: sentinel-dashboard:https://github.com/alibaba/Sentinel/releases
 * </pre>
 *
 * @author wang.ch
 * @date 2019-1-31 16:07:50
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.scio.cloud.sentinel")
public class ScioSentinelApplication {

  public static void main(String[] args) {
    SpringApplication.run(ScioSentinelApplication.class, args);
  }
  /**
   * test controller
   *
   * @author wang.ch
   * @date 2019-01-31 16:11:53
   */
  @RestController
  public static class MeController {

    private AtomicInteger num = new AtomicInteger(0);

    @RequestMapping("/me")
    public String me() {
      return "scio-cloud-sentinel";
    }

    @RequestMapping("/num")
    public int num() {
      return num.incrementAndGet();
    }
  }
}
