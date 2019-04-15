## scio
[![CircleCI](https://circleci.com/gh/rench/scio.svg?style=svg)](https://circleci.com/gh/rench/scio)
[![Auth](https://img.shields.io/badge/Author-Wang.ch-blue.svg)](https://xuankejia.cn)
[![GitHub stars](https://img.shields.io/github/stars/rench/scio.svg?style=social&label=Stars)](https://github.com/rench/scio)
[![GitHub forks](https://img.shields.io/github/forks/rench/scio.svg?style=social&label=Fork)](https://github.com/rench/scio)

# scio-cloud-jpa [中文](https://github.com/rench/scio/tree/master/scio-cloud-jpa/README_zh.md)
> https://github.com/rench/scio/tree/master/scio-cloud-jpa
------

The JpaRepository provided by SpringDataJpa is very powerful. The commonly used additions, deletions and changes have been fully used, but in actual development, some custom **Repository** is often used. For normal development, we only need to define **Repository** interface, plus a **@Repository** annotation, SpringDataJpa will automatically generate a proxy instance for Jpa for us. If we need to customize it, we need to make some changes and configuration.

> * SpringDataJPA mechanism and principle
> * How to write a custom Repository
> * How to use a custom Repository
> * Considerations for using a custom Repository

------

## SpringDataJPA mechanism and principle

JPA is the Java Persistence API, Java Persistence API, which is a set of interfaces and a set of standards introduced by Sun. Hibernate is a concrete ORM persistence layer framework that implements the JPA interface.

Spring-data-jpa has many advantages, such as inheriting the Repository interface, writing JPQL statements in annotations to access the database; supporting method name resolution to access the database; using Predicate to support dynamic queries, etc., not listed here. These are all advantages of using spring-data-jpa. To use it better and more elegant, we must explore the loading and running mechanism of spring-data-jpa.

Spring-data-jpa seamlessly interfaces with the springframework framework at runtime. When generating a Repository instance using Spring's @Repository annotation, the Repository interface is instantiated using the dynamic proxy class and placed in the spring container for later use. The main categories of concern are as follows:

- RepositoryFactoryBeanSupport
- RepositoryBeanDefinitionBuilder
- RepositoryFactorySupport
- RepositoryProxyPostProcessor

## How to write a custom Repository
> https://docs.spring.io/spring-data/jpa/docs/1.5.3.RELEASE/reference/html/repositories.html#repositories.custom-implementations

- **UserInfoJpaRepositoryCustom**, need to implement a custom Repository, also need to define an interface, and an implementation class. E.g:

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
- **UserInfoJpaRepositoryImpl**, you need to write an implementation class to implement a custom method.
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
> Custom implementations are automatically loaded and instantiated by SpringDataJpa, managed by Spring, and custom implementation classes can still use annotations to inject the required instances of the association.

- **UserInfoJpaRepository**, the interface that is mainly annotated by **@Repository** needs to be adjusted, as follows:

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
> In addition to inheriting the **JpaRepository** interface provided by SpringDatJpa, this interface also inherits **UserInfoJpaRepositoryCustom**.

## How to use a custom Repository

Use a custom Repository, the same as before without customization, and inject it where you need to inject the Repository.

## Considerations for using a custom Repository

1. When implementing a custom method, the SpringDataJpa default scan loads the interface class name +Impl annotated with **@Repository**. The configuration that can be changed is Impl, as follows:

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
2. Custom implementation classes do not need to manually add annotations such as **@Component**.
3. The processing logic for custom implementation class scans is:
> RepositoryBeanDefinitionBuilder::registerCustomImplementation
> CustomRepositoryImplementationDetector