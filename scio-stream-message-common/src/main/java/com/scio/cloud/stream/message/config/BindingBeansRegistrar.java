package com.scio.cloud.stream.message.config;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
/**
 * spring cloud binding-bean registrar with EnvironmentAware inject env to resolve application properties
 *
 * @see org.springframework.cloud.stream.config.BindingBeansRegistrar
 * @author wang.ch
 * @date 2019-02-02 08:27:59
 */
public class BindingBeansRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

  private Environment env;

  @Override
  public void registerBeanDefinitions(
      AnnotationMetadata metadata, BeanDefinitionRegistry registry) {

    AnnotationAttributes attrs =
        AnnotatedElementUtils.getMergedAnnotationAttributes(
            ClassUtils.resolveClassName(metadata.getClassName(), null), EnableBinding.class);
    for (Class<?> type : collectClasses(attrs, metadata.getClassName())) {
      BindingBeanDefinitionRegistryUtils.registerBindingTargetBeanDefinitions(
          type, type.getName(), registry, env);
      BindingBeanDefinitionRegistryUtils.registerBindingTargetsQualifiedBeanDefinitions(
          ClassUtils.resolveClassName(metadata.getClassName(), null), type, registry);
    }
  }

  private Class<?>[] collectClasses(AnnotationAttributes attrs, String className) {
    EnableBinding enableBinding =
        AnnotationUtils.synthesizeAnnotation(
            attrs, EnableBinding.class, ClassUtils.resolveClassName(className, null));
    return enableBinding.value();
  }

  @Override
  public void setEnvironment(Environment environment) {
    this.env = environment;
  }
}
