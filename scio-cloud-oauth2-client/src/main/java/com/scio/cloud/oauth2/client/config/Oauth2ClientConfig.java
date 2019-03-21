package com.scio.cloud.oauth2.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(-1)
public class Oauth2ClientConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf()
        .disable() //
        .httpBasic()
        .disable() //
        .formLogin()
        .disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers("/favicon.ico")
        .permitAll();
  }

  //  @Bean
  //  public OAuth2RestTemplate restTemplate(UserInfoRestTemplateFactory factory) {
  //    return factory.getUserInfoRestTemplate();
  //  }

  @Bean
  public OAuth2RestTemplate loadBalancedOauth2RestTemplate(
      OAuth2ProtectedResourceDetails resource, OAuth2ClientContext context) {
    ClientCredentialsResourceDetails detail = new ClientCredentialsResourceDetails();
    detail.setClientId(resource.getClientId());
    detail.setClientSecret(resource.getClientSecret());
    detail.setAccessTokenUri(resource.getAccessTokenUri());
    // The default context will generate one for each session. just use a new one
    return new OAuth2RestTemplate(detail, new DefaultOAuth2ClientContext());
  }

  // @Bean(name = "remoteRestTemplate")
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
