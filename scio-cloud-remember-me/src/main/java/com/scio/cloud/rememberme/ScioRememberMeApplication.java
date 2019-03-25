package com.scio.cloud.rememberme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
/**
 * scio-cloud-remember-me
 *
 * @doc
 *     https://docs.spring.io/spring-security/site/docs/5.2.0.BUILD-SNAPSHOT/reference/htmlsingle/#remember-me
 * @author Wang.ch
 * @date 2019-3-25 08:57:05
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.scio.cloud.rememberme")
public class ScioRememberMeApplication {

  public static void main(String[] args) {
    SpringApplication.run(ScioRememberMeApplication.class, args);
  }
}
