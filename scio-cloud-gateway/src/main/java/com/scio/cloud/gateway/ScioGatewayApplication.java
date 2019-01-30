package com.scio.cloud.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
/**
 * scio-cloud-gateway
 *
 * <pre>
 * Document: https://cloud.spring.io/spring-cloud-static/spring-cloud-gateway/2.1.0.RELEASE/single/spring-cloud-gateway.html
 * Dependencies: scio-eureka-server,scio-eureka-discovery,scio-config-server
 * </pre>
 *
 * @author wang.ch
 * @date 2019-01-24 15:28:27
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = "com.scio.cloud.gateway")
public class ScioGatewayApplication {

  public static void main(String[] args) {
    SpringApplication.run(ScioGatewayApplication.class, args);
  }
}
