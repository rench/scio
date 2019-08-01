package com.scio.cloud.querydsl;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.scio.cloud.querydsl.config.ScioJPAQueryFactory;
/**
 * scio-cloud-querydsl
 *
 * @doc 2.1.2 https://docs.spring.io/spring-data/jpa/docs/2.1.2.RELEASE/reference/html/
 * @doc http://www.querydsl.com/static/querydsl/3.4.3/reference/html_single/#jpa_integration
 * @author Wang.ch
 * @date 2019-4-17 09:20:17
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.scio.cloud.querydsl")
@EnableJpaRepositories(
    basePackages = "com.scio.cloud.querydsl",
    repositoryImplementationPostfix = "Impl")
public class ScioQuerydslApplication {

  public static void main(String[] args) {
    SpringApplication.run(ScioQuerydslApplication.class, args);
  }

  /**
   * return JPAQueryFactory
   *
   * @param entityManager
   * @return
   */
  @Bean
  @Autowired
  public JPAQueryFactory jpaQuery(EntityManager entityManager) {
    return new JPAQueryFactory(entityManager);
  }

  /**
   * return JPAQueryFactory
   *
   * @param entityManager
   * @return
   */
  @Bean
  @Autowired
  public ScioJPAQueryFactory scioJpaQuery(EntityManager entityManager) {
    return new ScioJPAQueryFactory(entityManager);
  }
}
