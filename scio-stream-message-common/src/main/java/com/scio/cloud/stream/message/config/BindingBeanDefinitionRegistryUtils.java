package com.scio.cloud.stream.message.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.beans.factory.support.AutowireCandidateQualifier;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.cloud.stream.annotation.Bindings;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.binding.BindableProxyFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.MethodCallback;
import org.springframework.util.StringUtils;

import com.scio.cloud.stream.message.util.AnnotationHelper;
import com.scio.cloud.stream.message.util.InputAnnotation;
import com.scio.cloud.stream.message.util.OutputAnnotation;
/**
 * spring cloud stream binding-bean registry for alter Input and Output Annotation
 *
 * @see org.springframework.cloud.stream.binding.BindingBeanDefinitionRegistryUtils
 * @author wang.ch
 * @date 2019-02-02 08:25:26
 */
@SuppressWarnings("deprecation")
public class BindingBeanDefinitionRegistryUtils {
  public static void registerInputBindingTargetBeanDefinition(
      String qualifierValue,
      String name,
      String bindingTargetInterfaceBeanName,
      String bindingTargetInterfaceMethodName,
      BeanDefinitionRegistry registry) {

    registerBindingTargetBeanDefinition(
        Input.class,
        qualifierValue,
        name,
        bindingTargetInterfaceBeanName,
        bindingTargetInterfaceMethodName,
        registry);
  }

  public static void registerOutputBindingTargetBeanDefinition(
      String qualifierValue,
      String name,
      String bindingTargetInterfaceBeanName,
      String bindingTargetInterfaceMethodName,
      BeanDefinitionRegistry registry) {
    registerBindingTargetBeanDefinition(
        Output.class,
        qualifierValue,
        name,
        bindingTargetInterfaceBeanName,
        bindingTargetInterfaceMethodName,
        registry);
  }

  private static void registerBindingTargetBeanDefinition(
      Class<? extends Annotation> qualifier,
      String qualifierValue,
      String name,
      String bindingTargetInterfaceBeanName,
      String bindingTargetInterfaceMethodName,
      BeanDefinitionRegistry registry) {

    RootBeanDefinition rootBeanDefinition = new RootBeanDefinition();
    rootBeanDefinition.setFactoryBeanName(bindingTargetInterfaceBeanName);
    rootBeanDefinition.setUniqueFactoryMethodName(bindingTargetInterfaceMethodName);
    rootBeanDefinition.addQualifier(new AutowireCandidateQualifier(qualifier, qualifierValue));
    registry.registerBeanDefinition(name, rootBeanDefinition);
  }

  public static void registerBindingTargetBeanDefinitions(
      Class<?> type,
      final String bindingTargetInterfaceBeanName,
      final BeanDefinitionRegistry registry,
      final Environment env) {
    ReflectionUtils.doWithMethods(
        type,
        new MethodCallback() {
          @Override
          public void doWith(Method method)
              throws IllegalArgumentException, IllegalAccessException {
            Input input = method.getAnnotation(Input.class);
            if (input != null) {
              alterInputAnnotation(env, method, input);
              input = AnnotationUtils.findAnnotation(method, Input.class);
              String name = getBindingTargetName(input, method);
              registerInputBindingTargetBeanDefinition(
                  input.value(), name, bindingTargetInterfaceBeanName, method.getName(), registry);
            }

            Output output = method.getDeclaredAnnotation(Output.class);
            if (output != null) {
              // must alter before `AnnotationUtils.findAnnotation`,because `findAnnotation` will
              // cache the annotation on method
              alterOutputAnnotation(env, method, output);
              output = AnnotationUtils.findAnnotation(method, Output.class);
              String name = getBindingTargetName(output, method);
              registerOutputBindingTargetBeanDefinition(
                  output.value(), name, bindingTargetInterfaceBeanName, method.getName(), registry);
            }
          }

          private void alterOutputAnnotation(final Environment env, Method method, Output output) {
            String name = output.value();
            if (name.startsWith("${") && name.endsWith("}")) {
              name = env.getProperty(name.substring(2, name.length() - 1), name);
              OutputAnnotation outputImpl = new OutputAnnotation();
              outputImpl.setValue(name);
              output = outputImpl;
              AnnotationHelper.alterAnnotationOnMethod(method, Output.class, outputImpl);
            }
          }

          private void alterInputAnnotation(final Environment env, Method method, Input input) {
            String name = input.value();
            if (name.startsWith("${") && name.endsWith("}")) {
              name = env.getProperty(name.substring(2, name.length() - 1), name);
              InputAnnotation inputImpl = new InputAnnotation();
              inputImpl.setValue(name);
              input = inputImpl;
              AnnotationHelper.alterAnnotationOnMethod(method, Input.class, inputImpl);
            }
          }
        });
  }

  public static void registerBindingTargetsQualifiedBeanDefinitions(
      Class<?> parent, Class<?> type, final BeanDefinitionRegistry registry) {

    if (type.isInterface()) {
      RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(BindableProxyFactory.class);
      rootBeanDefinition.addQualifier(new AutowireCandidateQualifier(Bindings.class, parent));
      rootBeanDefinition.getConstructorArgumentValues().addGenericArgumentValue(type);
      registry.registerBeanDefinition(type.getName(), rootBeanDefinition);
    } else {
      RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(type);
      rootBeanDefinition.addQualifier(new AutowireCandidateQualifier(Bindings.class, parent));
      registry.registerBeanDefinition(type.getName(), rootBeanDefinition);
    }
  }

  public static String getBindingTargetName(Annotation annotation, Method method) {
    Map<String, Object> attrs = AnnotationUtils.getAnnotationAttributes(annotation, false);
    if (attrs.containsKey("value") && StringUtils.hasText((CharSequence) attrs.get("value"))) {
      return (String) attrs.get("value");
    }
    return method.getName();
  }
}
