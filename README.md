# SCIO Spring Cloud In One
[![CircleCI](https://circleci.com/gh/rench/scio.svg?style=svg)](https://circleci.com/gh/rench/scio)
> Spring Cloud In One is a simple demo project that can also be used for rapid development of scaffolding projects. The project includes the project and technology stack that spring cloud currently has access to.

## projects
### config
- [x] scio-config-server 8000
- [x] scio-cloud-config 7999
- [x] scio-consul-config 7999
- [x] scio-nacos-config 7999
- [x] scio-zookeeper-config 7999
- [x] scio-apollo-config 7999

### registry
- [x] scio-eureka-server 8001
- [x] scio-eureka-discovery 7998
- [x] scio-nacos-discovery 7998
- [x] scio-zookeeper-discovery 7998

### gateway & route
- [x] scio-cloud-gateway 8002
- [x] scio-cloud-zuul 8002

### monitor & protection
- [x] scio-cloud-zuul-ratelimit 8002
- [x] scio-cloud-sentinel 7997
- [ ] spring-cloud-nextfix-hystrix(protection,monitor)
- [ ] spring-cloud-netflix-turbine(monitor)

### stream message

- [x] scio-stream-message-common
- [x] scio-stream-message-producer 7996
- [x] scio-stream-message-consumer 7995

### tracing
- [ ] spring-cloud-sleuth+zipkin
- [ ] spring-boot-admin
- [ ] pinpoint

### security
- [ ] spring-cloud-security(oauth2.0)
- [ ] spring-remember-me
- [ ] json-web-token

### orm
- [ ] jpa
- [ ] querydsl
- [ ] jooq
- [ ] mybatis-plus

### distributed database
- [ ] sharding-jdbc
- [ ] mycat

### distributed transaction
- [ ] byteTCC
- [ ] raincat
- [ ] hmily
- [ ] myth

### distributed lock
- [ ] redisson-redis-lock





## docs

- https://github.com/spring-cloud-incubator/spring-cloud-alibaba
- https://github.com/marcosbarbero/spring-cloud-zuul-ratelimit