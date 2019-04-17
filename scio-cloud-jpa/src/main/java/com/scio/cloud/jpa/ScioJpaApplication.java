package com.scio.cloud.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
/**
 * scio-cloud-jpa
 *
 * @doc 2.1.2 https://docs.spring.io/spring-data/jpa/docs/2.1.2.RELEASE/reference/html/
 * @author Wang.ch
 * @date 2019-4-15 09:15:52
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.scio.cloud.jpa")
@EnableJpaRepositories(
    basePackages = "com.scio.cloud.jpa",
    repositoryImplementationPostfix = "Impl")
public class ScioJpaApplication {

  public static void main(String[] args) {
    SpringApplication.run(ScioJpaApplication.class, args);
  }
}
