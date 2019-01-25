package com.scio.cloud.zkconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
/**
 * zookeeper config client demo
 * <pre>
 * Document: https://cloud.spring.io/spring-cloud-static/spring-cloud-zookeeper/2.1.0.RELEASE/single/spring-cloud-zookeeper.html#spring-cloud-zookeeper-config
 * Zookeeper: http://zookeeper.apache.org/
 * </pre>
 * @author wang.ch
 * @date 2019-1-25 15:42:17
 */
@SpringBootApplication
@RefreshScope
public class ScioZkConfigApplication {

  @Value("${scio.username}")
  private String username;

  @Value("${scio.password}")
  private String password;

  public static void main(String[] args) {
    SpringApplication.run(ScioZkConfigApplication.class, args);
  }
}
