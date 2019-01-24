package com.scio.cloud.consulconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
