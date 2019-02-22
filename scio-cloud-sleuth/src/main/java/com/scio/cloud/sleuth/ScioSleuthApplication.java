package com.scio.cloud.sleuth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
/**
 * scio-cloud-sleuth
 *
 * <pre>
 * Document: https://cloud.spring.io/spring-cloud-static/spring-cloud-sleuth/2.1.0.RELEASE/single/spring-cloud-sleuth.html#_only_sleuth_log_correlation
 * Dependencies: scio-eureka-server
 * </pre>
 *
 * @author Wang.ch
 * @date 2019-2-21 16:56:33
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = "com.scio.cloud.sleuth")
public class ScioSleuthApplication {

  public static void main(String[] args) {
    SpringApplication.run(ScioSleuthApplication.class, args);
  }

  /**
   * restTemplate with loadBalance
   *
   * @return
   */
  @Bean
  @LoadBalanced
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
  /**
   * demo controller
   *
   * @author Wang.ch
   * @date 2019-02-22 09:26:57
   */
  @RestController
  static class DemoController {

    @Autowired private RestTemplate restTemplate;
    /**
     * me
     *
     * @return
     */
    @RequestMapping("/me")
    public String me() {
      return "scio-cloud-sleuth";
    }
    /**
     * request for other me
     *
     * @return
     */
    @RequestMapping("/other")
    public String other() {
      return restTemplate.getForEntity("http://scio-cloud-hystrix/me", String.class).getBody();
    }
  }
}
