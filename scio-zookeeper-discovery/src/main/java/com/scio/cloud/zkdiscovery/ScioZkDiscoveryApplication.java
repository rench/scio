package com.scio.cloud.zkdiscovery;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
/**
 * zookeeper discovery client demo
 *
 * <pre>
 * Document: https://cloud.spring.io/spring-cloud-static/spring-cloud-zookeeper/2.1.0.RELEASE/single/spring-cloud-zookeeper.html#spring-cloud-zookeeper-discovery
 * Document: https://dzone.com/articles/spring-cloud-config-series-part-2-git-backend
 * Document: http://www.enriquerecarte.com/2017-07-21/spring-cloud-config-series-introduction
 * Document: https://dzone.com/articles/spring-cloud-config-part-3-zookeeper-backend
 * Zookeeper: http://zookeeper.apache.org/
 * </pre>
 *
 * @author wang.ch
 * @date 2019-1-25 15:42:17
 */
@SpringBootApplication
@RefreshScope
@RestController
@EnableDiscoveryClient
public class ScioZkDiscoveryApplication {

  /**
   *
   *
   * <pre>
   * path store in zookeeper znode: /configuration/scio-zookeeper-config,dev/scio.username
   * znodedata: username
   * </pre>
   */
  @Value("${scio.username:scio}")
  private String username;
  /**
   *
   *
   * <pre>
   * path store in zookeeper: /configuration/scio-zookeeper-config,dev/scio.password
   * znodedata: password
   * </pre>
   */
  @Value("${scio.password:scio}")
  private String password;

  @RequestMapping("/config")
  @ResponseBody
  public String username() {
    return this.username + '@' + this.password;
  }

  public static void main(String[] args) {
    SpringApplication.run(ScioZkDiscoveryApplication.class, args);
  }
}
