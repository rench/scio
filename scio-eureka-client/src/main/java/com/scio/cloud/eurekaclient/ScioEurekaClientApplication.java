package com.scio.cloud.eurekaclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
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
@EnableEurekaClient
public class ScioEurekaClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(ScioEurekaClientApplication.class, args);
  }
}
