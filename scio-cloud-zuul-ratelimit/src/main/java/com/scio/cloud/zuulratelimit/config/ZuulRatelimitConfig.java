package com.scio.cloud.zuulratelimit.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.repository.DefaultRateLimiterErrorHandler;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.repository.RateLimiterErrorHandler;
/**
 * zuul ratelimit config
 *
 * @author wang.ch
 * @date 2019-01-31 14:51:54
 */
@Configuration
public class ZuulRatelimitConfig {
  private static final Logger LOG = LoggerFactory.getLogger(ZuulRatelimitConfig.class);

  @Bean
  public RateLimiterErrorHandler rateLimitErrorHandler() {
    return new DefaultRateLimiterErrorHandler() {
      @Override
      public void handleSaveError(String key, Exception e) {
        // custom code
        LOG.error("scio-cloud-zuul-ratelimit save rate error", e);
      }

      @Override
      public void handleFetchError(String key, Exception e) {
        // custom code
        LOG.error("scio-cloud-zuul-ratelimit fetch rate error", e);
      }

      @Override
      public void handleError(String msg, Exception e) {
        // custom code
        LOG.error("scio-cloud-zuul-ratelimit ratelimit error", e);
      }
    };
  }
}
