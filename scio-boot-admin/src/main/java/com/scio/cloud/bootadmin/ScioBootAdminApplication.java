package com.scio.cloud.bootadmin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
/**
 * scio-boot-admin
 *
 * <pre>
 * Document: http://codecentric.github.io/spring-boot-admin/2.1.3/#getting-started
 * dependencies: scio-eureka-server
 * </pre>
 *
 * @author Wang.ch
 * @date 2019-2-26 11:40:23
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableAdminServer
public class ScioBootAdminApplication {

  public static void main(String[] args) {
    SpringApplication.run(ScioBootAdminApplication.class, args);
  }

  /**
   * http://codecentric.github.io/spring-boot-admin/2.1.3/#register-client-applications
   *
   * @author Wang.ch
   * @date 2019-02-26 11:43:27
   */
  @Configuration
  public static class SecurityPermitAllConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.authorizeRequests().anyRequest().permitAll().and().csrf().disable();
    }
  }

  @RestController
  static class DemoController {
    @Value("${spring.application.name}")
    private String applicationName;

    @RequestMapping("/me")
    @ResponseBody
    public String me() {
      return applicationName;
    }
  }
}
