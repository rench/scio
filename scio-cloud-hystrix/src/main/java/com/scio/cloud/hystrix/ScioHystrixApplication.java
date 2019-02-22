package com.scio.cloud.hystrix;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
@EnableTurbine
@EnableFeignClients
@ComponentScan(basePackages = "com.scio.cloud.hystrix")
public class ScioHystrixApplication {

  public static void main(String[] args) {
    SpringApplication.run(ScioHystrixApplication.class, args);
  }
  /**
   * Feign controller
   *
   * @author Wang.ch
   * @date 2019-02-22 09:49:45
   */
  @RestController
  static class FeignController {

    @Autowired FeignService service;
    /**
     * request to scio-cloud-sleuth
     *
     * @return
     */
    @RequestMapping("/other")
    public String other() {
      return service.me();
    }
  }

  @FeignClient("scio-cloud-sleuth")
  static interface FeignService {
    @GetMapping("/me")
    String me();
  }

  /**
   * hystrix test controller
   *
   * @author Wang.ch
   * @date 2019-02-22 09:47:21
   */
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
    /**
     * fallback method
     *
     * @return
     */
    public String fallback() {
      LOG.info(
          "fallback :{}-{}",
          Thread.currentThread().getThreadGroup().getName(),
          Thread.currentThread().getName());
      return "busy";
    }
  }
  /**
   * turbine controller
   *
   * @author Wang.ch
   * @date 2019-02-22 09:48:45
   */
  @Controller
  static class WebController {
    private Integer streamPort;
    private String clusterName;

    @Autowired
    public WebController(
        @Value("${turbine.stream.port}") Integer streamPort,
        @Value("${turbine.aggregator.clusterConfig:default}") String clusterName) {
      this.streamPort = streamPort;
      this.clusterName = clusterName;
    }

    @RequestMapping(path = {"", "/"})
    public String index() {
      // redirect custom port.
      // turbine stream aggregator is installed in the same node as hystrix dashboard
      return "redirect:/hystrix/monitor?stream=http://localhost:"
          + streamPort
          + "/turbine.stream?cluster="
          + clusterName;
    }
  }
}
