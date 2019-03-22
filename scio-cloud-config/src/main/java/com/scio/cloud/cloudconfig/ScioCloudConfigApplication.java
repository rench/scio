package com.scio.cloud.cloudconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
/**
 * scio-cloud-config
 *
 * @author wang.ch
 * @date 2019-1-29 17:06:57
 */
@SpringBootApplication
@PropertySource(
    value = {"core.yml", "key.properties"},
    factory = CompositePropertySourceFactory.class)
public class ScioCloudConfigApplication {

  public static void main(String[] args) {
    SpringApplication.run(ScioCloudConfigApplication.class, args);
  }
}
