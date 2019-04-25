package com.scio.cloud.mybatisplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
/**
 * scio-cloud-mybatisplus
 *
 * @doc https://mp.baomidou.com/guide/generator.html#%E6%B7%BB%E5%8A%A0%E4%BE%9D%E8%B5%96
 * @doc 2.1.2 https://docs.spring.io/spring-data/jpa/docs/2.1.2.RELEASE/reference/html/
 * @doc https://mp.baomidou.com/
 * @author Wang.ch
 * @date 2019-4-25 09:38:29
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.scio.cloud.mybatisplus")
@MapperScan(basePackages = "com.scio.cloud.mybatisplus.mapper")
public class ScioMybatisplusApplication {

  public static void main(String[] args) {
    SpringApplication.run(ScioMybatisplusApplication.class, args);
  }
}
