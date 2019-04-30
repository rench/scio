package com.scio.cloud.shardingjdbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
/**
 * scio-cloud-shardingjdbc
 *
 * @doc 2.1.2 https://docs.spring.io/spring-data/jpa/docs/2.1.2.RELEASE/reference/html/
 * @author Wang.ch
 * @date 2019-4-15 09:15:52
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.scio.cloud.shardingjdbc")
@EnableJpaRepositories(
    basePackages = "com.scio.cloud.shardingjdbc",
    repositoryImplementationPostfix = "Impl")
public class ScioShardingjdbcApplication {

  public static void main(String[] args) {
    SpringApplication.run(ScioShardingjdbcApplication.class, args);
  }
}
