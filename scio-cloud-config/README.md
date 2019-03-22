# Cause
In the project, there is a file core.yml, which stores some basic configuration of the core module. The file is written in the same way as the yaml. E.g:
```
···
scio.core.api.secrets: test,123456
scio.core.sms.appKey=1d5123123165db5
···
```
In the eclipse editor, errors are often prompted, which means that the syntax in the yml file is incorrect. Indeed, `scio.core.sms.appKey=1d5123123165db5` is written in terms of properties. But start the program, no errors are reported, look bad, I want to cancel the error, so change `scio.core.sms.appKey=1d5123123165db5` to `scio.core.sms.appKey: "1d5123123165db5"`. Start the project, junit test error, suggesting that appKey does not exist.

# Find
Found junit test error, find the business logic of verifying appKey according to the problem, and found that the appKey obtained by `${scio.core.sms.appKey}` is actually ``1d5123123165db5"` with quotes. That's weird. According to yaml, double quotes and single quotes are just distinguishing between special symbols in escaped strings, and do not put double quotes inside the value. Tracking found that the reference to core.yml was parsed using `@PropertySource({"classpath:core.yml"})`, by looking at the explanation of the `@PropertySource` annotation, as follows: `Given a file app.properties containing The key/value pair testbean.name=myTestBean, the following @Configuration classuses @PropertySource to contribute app.properties to the Environment's set of PropertySources. `, it can be seen that by default he is the parsed properties file, guess, he did not follow Yaml format parsing, if the parsing of properties also supports colon separation, then the data parsing after the colon with quotation marks, can also explain.

# Analysis
- In the annotation of **@PropertySource**, there is a `factory` attribute that specifies a custom `PropertySourceFactory` interface implementation for parsing the specified file. The default implementation is `DefaultPropertySourceFactory`, continue to follow up, using `PropertiesLoaderUtils.loadProperties` for file parsing, so the default is to use Properties for parsing.

# Expansion
- **CompositePropertySourceFactory** After checking the parsing method of `DefaultPropertySourceFactory`, I found that it supports the parsing of the properties file. The load method of the follow-up properties found that when parsing, the delimiter is `=` or `:`, although it can be parsed simply. Yml format content, but can not support the real yaml syntax, you can extend the `DefaultPropertySourceFactory`, support mixed parsing of the two formats. The following is the main code

```
package com.scio.cloud.cloudconfig;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;
/**
 * CompositePropertySourceFactory support properties and yaml file
 *
 * @author Wang.ch
 * @date 2019-03-22 09:30:15
 */
public class CompositePropertySourceFactory extends DefaultPropertySourceFactory {
  @Override
  public PropertySource<?> createPropertySource(String name, EncodedResource resource)
      throws IOException {
    String sourceName = Optional.ofNullable(name).orElse(resource.getResource().getFilename());
    if (!resource.getResource().exists()) {
      // return an empty Properties
      return new PropertiesPropertySource(sourceName, new Properties());
    } else if (sourceName.endsWith(".yml") || sourceName.endsWith(".yaml")) {
      Properties propertiesFromYaml = loadYaml(resource);
      return new PropertiesPropertySource(sourceName, propertiesFromYaml);
    } else {
      return super.createPropertySource(name, resource);
    }
  }
  /**
   * load yaml file to properties
   *
   * @param resource
   * @return
   * @throws IOException
   */
  private Properties loadYaml(EncodedResource resource) throws IOException {
    YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
    factory.setResources(resource.getResource());
    factory.afterPropertiesSet();
    return factory.getObject();
  }
}
```
- **@PropertySource**
```
@PropertySource(
    value = {"core.yml", "key.properties"},
    factory = CompositePropertySourceFactory.class)
```