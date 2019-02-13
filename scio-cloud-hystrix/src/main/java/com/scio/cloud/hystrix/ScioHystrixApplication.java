package com.scio.cloud.hystrix;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
/**
 * scio-cloud-hystrix
 *
 * <pre>
 * Document: https://cloud.spring.io/spring-cloud-static/spring-cloud-netflix/2.1.0.RELEASE/single/spring-cloud-netflix.html#_how_to_include_hystrix
 * </pre>
 *
 * @author wang.ch
 * @date 2019-2-13 14:25:16
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableHystrixDashboard
@ComponentScan(basePackages = "com.scio.cloud.hystrix")
public class ScioHystrixApplication {

  public static void main(String[] args) {
    SpringApplication.run(ScioHystrixApplication.class, args);
  }

  @RestController
  public static class HystrixController {
    private static Logger LOG = LoggerFactory.getLogger(HystrixController.class);
    private AtomicInteger atom = new AtomicInteger(0);

    @RequestMapping("/me")
    @HystrixCommand(
        fallbackMethod = "fallback",
        commandProperties = {
          @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
        })
    // execution.isolation.strategy,THREAD|SEMAPHORE
    // https://github.com/Netflix/Hystrix/wiki/Configuration#executionisolationstrategy
    public String me() {
      LOG.info(
          "me :{}-{}",
          Thread.currentThread().getThreadGroup().getName(),
          Thread.currentThread().getName());
      if (atom.getAndIncrement() % 3 == 0) {
        throw new RuntimeException("should fallback");
      }
      return "scio-cloud-hystrix";
    }

    public String fallback() {
      LOG.info(
          "fallback :{}-{}",
          Thread.currentThread().getThreadGroup().getName(),
          Thread.currentThread().getName());
      return "busy";
    }
  }
}
