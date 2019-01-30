package com.scio.cloud.eurekadiscovery;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
/**
 * scio-eureka-client
 *
 * <pre>
 * Document: https://cloud.spring.io/spring-cloud-static/spring-cloud-netflix/2.1.0.RELEASE/single/spring-cloud-netflix.html#netflix-eureka-client-starter
 * Config: org.springframework.cloud.netflix.eureka.server.EurekaServerConfigBean
 * </pre>
 *
 * @author wang.ch
 * @date 2019-1-29 16:23:42
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ScioEurekaDiscoveryApplication {

  public static void main(String[] args) {
    SpringApplication.run(ScioEurekaDiscoveryApplication.class, args);
  }

  @RestController
  static class DemoController {
    @Value("${spring.application.name}")
    private String applicationName;

    @RequestMapping("/me")
    @ResponseBody
    public String me() {
      return applicationName;
    }
  }
}
