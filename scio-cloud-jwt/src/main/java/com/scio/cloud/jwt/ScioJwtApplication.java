package com.scio.cloud.jwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
/**
 * scio-cloud-jwt
 *
 * <pre>
 * Document: https://cloud.spring.io/spring-cloud-static/spring-cloud-netflix/2.1.0.RELEASE/single/spring-cloud-netflix.html#netflix-zuul-starter
 * @author Wang.ch
 * @date 2019-3-20 09:21:54
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.scio.cloud.jwt")
public class ScioJwtApplication {

  public static void main(String[] args) {
    SpringApplication.run(ScioJwtApplication.class, args);
  }
}
