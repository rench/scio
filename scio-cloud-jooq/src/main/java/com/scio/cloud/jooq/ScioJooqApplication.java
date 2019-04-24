package com.scio.cloud.jooq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
/**
 * scio-cloud-jooq
 *
 * @doc gradle scio-cloud-jooq:generateDbUserJooqSchemaSource
 * @doc 2.1.2 https://docs.spring.io/spring-data/jpa/docs/2.1.2.RELEASE/reference/html/
 * @doc http://www.jooq.org/
 * @author Wang.ch
 * @date 2019-4-17 09:20:17
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.scio.cloud.jooq")
public class ScioJooqApplication {

  public static void main(String[] args) {
    SpringApplication.run(ScioJooqApplication.class, args);
  }
}
