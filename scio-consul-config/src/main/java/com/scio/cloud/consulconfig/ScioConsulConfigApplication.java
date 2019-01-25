package com.scio.cloud.consulconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * consul config client demo
 * <pre>
 * Document: https://cloud.spring.io/spring-cloud-static/spring-cloud-consul/2.1.0.RELEASE/single/spring-cloud-consul.html#spring-cloud-consul-config
 * Consul: https://www.consul.io/downloads.html
 * </pre>
 * @author wang.ch
 * @date 2019-01-25 15:33:11
 */
@SpringBootApplication
public class ScioConsulConfigApplication {

  //consul key in store : configuration/scio-consul-config/dev/cfg
  @Value("${scio.username}")
  private String username;

  @Value("${scio.password}")
  private String password;

  public static void main(String[] args) {
    SpringApplication.run(ScioConsulConfigApplication.class, args);
  }
}
