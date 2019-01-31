package com.scio.cloud.zuulratelimit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;
/**
 * scio-cloud-zuul-ratelimit
 *
 * <pre>
 * Document: https://cloud.spring.io/spring-cloud-static/spring-cloud-netflix/2.1.0.RELEASE/single/spring-cloud-netflix.html#netflix-zuul-starter
 * Document: https://github.com/marcosbarbero/spring-cloud-zuul-ratelimit
 * Dependencies: scio-eureka-server,scio-eureka-discovery,scio-config-server
 * </pre>
 *
 * @author wang.ch
 * @date 2019-1-31 14:49:15
 */
@SpringBootApplication
@EnableZuulProxy
@ComponentScan(basePackages = "com.scio.cloud.zuulratelimit")
public class ScioZuulRatelimitApplication {

  public static void main(String[] args) {
    SpringApplication.run(ScioZuulRatelimitApplication.class, args);
  }
}
