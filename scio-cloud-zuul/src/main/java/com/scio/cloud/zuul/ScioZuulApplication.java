package com.scio.cloud.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;
/**
 * scio-cloud-zuul
 *
 * <pre>
 * Document: https://cloud.spring.io/spring-cloud-static/spring-cloud-netflix/2.1.0.RELEASE/single/spring-cloud-netflix.html#netflix-zuul-starter
 * Dependencies: scio-eureka-server,scio-eureka-discovery,scio-config-server
 * </pre>
 *
 * @author wang.ch
 * @date 2019-01-24 15:28:27
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
@ComponentScan(basePackages = "com.scio.cloud.zuul")
public class ScioZuulApplication {

  public static void main(String[] args) {
    SpringApplication.run(ScioZuulApplication.class, args);
  }
}
