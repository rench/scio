package com.scio.cloud.oauth2.resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
/**
 * @author Wang.ch
 * @date 2019-03-21 09:17:30
 */
@Configuration
@EnableResourceServer
// @EnableOAuth2Client
public class ScioOauth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

  @Override
  public void configure(HttpSecurity http) throws Exception {
    // define which resource will be protected by oauth2
    http.authorizeRequests().antMatchers("/userinfo").authenticated();
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    // UserInfoTokenServices to fetch user info from oauth2 server
  }
}
