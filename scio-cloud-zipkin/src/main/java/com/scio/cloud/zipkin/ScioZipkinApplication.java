package com.scio.cloud.zipkin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import zipkin2.server.internal.EnableZipkinServer;
/**
 * scio-cloud-zipkin
 *
 * <pre>
 * Document: https://cloud.spring.io/spring-cloud-static/spring-cloud-sleuth/2.1.0.RELEASE/single/spring-cloud-sleuth.html#_only_sleuth_log_correlation
 * Dependencies: scio-eureka-server
 * </pre>
 *
 * @author Wang.ch
 * @date 2019-2-21 17:14:00
 */
@SpringBootApplication
@EnableZipkinServer
@ComponentScan(basePackages = "com.scio.cloud.zipkin")
public class ScioZipkinApplication {

  public static void main(String[] args) {
    SpringApplication.run(ScioZipkinApplication.class, args);
  }
}
