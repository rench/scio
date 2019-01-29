package com.scio.cloud.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
/**
 * scio-config
 * @author wang.ch
 * @date 2019-01-24 15:28:27
 */
@SpringBootApplication
@EnableConfigServer
public class ScioConfigApplication {

  public static void main(String[] args) {
    SpringApplication.run(ScioConfigApplication.class, args);
  }
}
