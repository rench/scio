package com.scio.cloud.nacosconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
/**
 * nacos config client demo
 * <pre>
 * Document: https://github.com/spring-cloud-incubator/spring-cloud-alibaba/wiki/Nacos-config
 * Nacos: https://github.com/alibaba/nacos/releases
 * </pre>
 * @author wang.ch
 * @date 2019-1-25 15:11:23
 */
@SpringBootApplication
@RefreshScope
public class ScioNacosConfigApplication {

  // file key in store : scio-nacos-config-dev.yaml DEFAULT_GROUP
  @Value("${scio.username}")
  private String username;

  @Value("${scio.password}")
  private String password;

  public static void main(String[] args) {
    SpringApplication.run(ScioNacosConfigApplication.class, args);
  }
}
