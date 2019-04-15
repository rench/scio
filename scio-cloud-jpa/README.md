## scio
[![CircleCI](https://circleci.com/gh/rench/scio.svg?style=svg)](https://circleci.com/gh/rench/scio)
[![Auth](https://img.shields.io/badge/Author-Wang.ch-blue.svg)](https://xuankejia.cn)
[![GitHub stars](https://img.shields.io/github/stars/rench/scio.svg?style=social&label=Stars)](https://github.com/rench/scio)
[![GitHub forks](https://img.shields.io/github/forks/rench/scio.svg?style=social&label=Fork)](https://github.com/rench/scio)

# scio-cloud-jpa [English](https://github.com/rench/scio/tree/master/scio-cloud-jpa/README.md)
> https://github.com/rench/scio/tree/master/scio-cloud-jpa
------

SpringDataJpa提供的JpaRepository已经非常强大，常用的增删查改已经完全够用，但是在实际开发中，经常会用到一些自定义的**Repository**，正常的开发时，我们只需要定义**Repository**接口，加上一个**@Repository**注解，SpringDataJpa则会自动帮我们生成Jpa的代理实例，如果我们需要自定义，则需要做一些改变和配置。

> * SpringDataJPA机制和原理
> * 如何编写自定义Repository
> * 如何使用自定义的Repository
> * 使用自定义Repository需要注意的地方

------

## SpringDataJPA机制和原理

JPA是Java Persistence API，Java持久化API，是SUN公司推出的一套接口，一套标准。Hibernate是一个具体的ORM的持久层框架，实现了JPA接口。

spring-data-jpa的优点很多，比如继承Repository接口，在注解中书写JPQL语句即可访问数据库；支持方法名解析方式访问数据库；使用Predicate支持动态查询等，在此不一一列举了。这些都是使用spring-data-jpa中的种种优点，要想将之使用的更好更优雅，就要从spring-data-jpa的加载和运行机制进行探秘。

spring-data-jpa在运行时和springframework框架实现了无缝对接。在使用spring的@Repository注解生成Repository实例时，使用动态代理类的方式对Repository接口进行了实例化并放入spring容器中备用。主要关注的几个类，如下：

- RepositoryFactoryBeanSupport
- RepositoryBeanDefinitionBuilder
- RepositoryFactorySupport
- RepositoryProxyPostProcessor

## 如何编写自定义的Repository
> https://docs.spring.io/spring-data/jpa/docs/1.5.3.RELEASE/reference/html/repositories.html#repositories.custom-implementations

- **UserInfoJpaRepositoryCustom**，需要实现自定义的Repository，同样需要定义个接口，和一个实现类。例如：

```
/**
 * user info jpa repository custom
 *
 * @author Wang.ch
 * @date 2019-04-15 09:46:13
 */
public interface UserInfoJpaRepositoryCustom {
  /**
   * batch save user info
   *
   * @param userinfos
   * @return
   */
  List<UserInfo> batchSave(List<UserInfo> userinfos);
}
```
- **UserInfoJpaRepositoryImpl**，需要编写一个实现类，实现自定义方法。
```
/**
 * user info custom implementation
 *
 * @author Wang.ch
 * @date 2019-04-15 09:48:38
 */
public class UserInfoJpaRepositoryImpl implements UserInfoJpaRepositoryCustom {

  @Autowired private EntityManager entityManager;

  @Override
  @Transactional
  public List<UserInfo> batchSave(List<UserInfo> userinfos) {
    userinfos.stream().forEach(entityManager::persist);
    return userinfos;
  }
}
```
> 自定义的实现方法会被SpringDataJpa自动加载和实例化，由Spring管理，自定义实现类依然可以使用注解注入需要的关联的实例。
- **UserInfoJpaRepository**，主要被**@Repository**注解的接口需要进行调整，如下：
```
/**
 * user info jpa repository
 *
 * @author Wang.ch
 * @date 2019-04-15 09:56:08
 */
@Repository
public interface UserInfoJpaRepository
    extends JpaRepository<UserInfo, Long>, UserInfoJpaRepositoryCustom {
  /**
   * find user info by username
   *
   * @param username
   * @return
   */
  Optional<UserInfo> findByUsername(String username);
}
```
> 该接口除了继承SpringDatJpa提供的**JpaRepository**接口外，还继承了**UserInfoJpaRepositoryCustom**
。

## 如何使用自定义的Repository

使用自定义Repository，和没有自定义以前是一样的，在需要注入Repository的地方注入即可。

## 使用自定义Repository需要注意的地方

1. 在实现自定义方法的时候，SpringDataJpa默认扫描加载的是是以**@Repository**注解的接口类名称+Impl。可以更改配置的是Impl，如下：
```
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
```
2. 自定义实现类不需要手动添加**@Component**等注解。
3. 自定义实现类扫描的处理逻辑在：
> RepositoryBeanDefinitionBuilder::registerCustomImplementation
> CustomRepositoryImplementationDetector