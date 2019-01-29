package com.scio.cloud.apolloconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
/**
 * apollo config client demo
 *
 * <pre>
 * Document: https://github.com/ctripcorp/apollo/wiki
 * Apollo: https://github.com/ctripcorp/apollo/wiki
 * </pre>
 *
 * @author wang.ch
 * @date 2019-1-25 15:11:23
 */
@SpringBootApplication
@RefreshScope
public class ScioApolloConfigApplication {

  @Value("${scio.username}")
  private String username;

  @Value("${scio.password}")
  private String password;

  public static void main(String[] args) {
    SpringApplication.run(ScioApolloConfigApplication.class, args);
  }
}
