package com.scio.cloud.configserver;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.bind.PropertySourcesPlaceholdersResolver;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.boot.context.properties.source.ConfigurationPropertyNameAliases;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import com.google.common.collect.Lists;
/**
 * scio-config-server
 *
 * @author wang.ch
 * @date 2019-01-24 15:28:27
 */
@SpringBootApplication
@EnableConfigServer
public class ScioConfigServerApplication {
  private static final Logger LOG = LoggerFactory.getLogger(ScioConfigServerApplication.class);
  // https://www.jianshu.com/p/cac4759b2684
  @Autowired private Environment env;

  public static void main(String[] args) {
    ApplicationContext ctx = SpringApplication.run(ScioConfigServerApplication.class, args);
    Config cfg = ctx.getBean(Config.class);
    LOG.info("cfg.username:{}", cfg.getUsername());
  }

  @SuppressWarnings("unchecked")
  @Bean
  @ConfigurationProperties("scio.cloud.config")
  public Config config() {
    Config cfg = new Config();
    // case 1
    Binder binder = Binder.get(env);
    Map<String, String> map = binder.bind("scio.cloud.config", Map.class).get();
    ConfigurationPropertySource source = new MapConfigurationPropertySource(map);
    ConfigurationPropertyNameAliases aliases = new ConfigurationPropertyNameAliases();
    aliases.addAliases("user", "username");
    binder = new Binder(source.withAliases(aliases));
    binder.bind(ConfigurationPropertyName.EMPTY, Bindable.ofInstance(cfg));

    // case 2
    Iterable<ConfigurationPropertySource> it = ConfigurationPropertySources.get(env);
    List<ConfigurationPropertySource> list = Lists.newArrayList(it);
    aliases = new ConfigurationPropertyNameAliases();
    aliases.addAliases("user", "username");
    list.add(source.withAliases(aliases));
    Binder binder2 = new Binder(list, new PropertySourcesPlaceholdersResolver(env));
    binder2.bind("scio.cloud.config", Bindable.ofInstance(cfg));

    return cfg;
  }

  static class Config {
    private String username;

    /** @return the username */
    public String getUsername() {
      return username;
    }

    /** @param username the username to set */
    public void setUsername(String username) {
      this.username = username;
    }
  }
}
