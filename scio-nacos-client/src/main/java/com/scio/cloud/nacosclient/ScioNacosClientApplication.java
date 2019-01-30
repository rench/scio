package com.scio.cloud.nacosclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
/**
 * nacos discovery client demo
 *
 * <pre>
 * Document: https://github.com/spring-cloud-incubator/spring-cloud-alibaba/wiki/Nacos-discovery
 * Nacos: https://github.com/alibaba/nacos/releases
 * </pre>
 *
 * @author wang.ch
 * @date 2019-1-30 11:22:06
 */
@SpringBootApplication
@RefreshScope
@EnableDiscoveryClient
public class ScioNacosClientApplication {

  @Value("${scio.username:scio}")
  private String username;

  @Value("${scio.password:scio}")
  private String password;

  public static void main(String[] args) {
    SpringApplication.run(ScioNacosClientApplication.class, args);
  }
}
