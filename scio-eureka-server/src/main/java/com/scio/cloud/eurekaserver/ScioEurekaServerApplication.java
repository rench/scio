package com.scio.cloud.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
/**
 * scio-eureka-server
 *
 * <pre>
 * Document: https://cloud.spring.io/spring-cloud-static/spring-cloud-netflix/2.1.0.RELEASE/single/spring-cloud-netflix.html#spring-cloud-eureka-server
 * Config: org.springframework.cloud.netflix.eureka.server.EurekaServerConfigBean
 * </pre>
 * 
 * @author wang.ch
 * @date 2019-1-29 16:23:42
 */
@SpringBootApplication
@EnableEurekaServer
public class ScioEurekaServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ScioEurekaServerApplication.class, args);
  }
}
