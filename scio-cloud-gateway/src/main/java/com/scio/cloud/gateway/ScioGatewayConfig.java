package com.scio.cloud.gateway;

import org.springframework.cloud.gateway.filter.factory.StripPrefixGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
/**
 * customRouteLocator
 *
 * <pre>
 * Doc: https://cloud.spring.io/spring-cloud-static/spring-cloud-gateway/2.1.0.RELEASE/single/spring-cloud-gateway.html#_fluent_java_routes_api
 * </pre>
 *
 * @author wang.ch
 * @date 2019-01-30 15:22:33
 */
@Configuration
@EnableWebFlux
public class ScioGatewayConfig {

  @Bean
  public RouteLocator customRouteLocator(
      RouteLocatorBuilder builder, StripPrefixGatewayFilterFactory stripPrefix) {
    return builder
        .routes()
        // via http://localhost:8002/scio-eureka-discovery-mirror/me
        .route(
            t ->
                t.path("/scio-eureka-discovery-mirror/**")
                    .filters(f -> f.stripPrefix(1))
                    .uri("http://localhost:7998"))
        // via http://localhost:8002/scio-eureka-discovery-mirror2/y(*)/me
        .route(
            t ->
                t.path("/scio-eureka-discovery-mirror2/**")
                    .filters(f -> f.filter(stripPrefix.apply(c -> c.setParts(2))))
                    .uri("http://localhost:7998"))
        .build();
  }
}
