package com.scio.cloud.jooq.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

/**
 * ScioRememberMeSecurityConfig
 *
 * @doc
 *     https://docs.spring.io/spring-security/site/docs/5.2.0.BUILD-SNAPSHOT/reference/htmlsingle/#remember-me
 * @author Wang.ch
 * @date 2019-03-25 08:58:03
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ScioSecurityConfig extends WebSecurityConfigurerAdapter {
  private static final String SECRET = "scio@2019";
  @Autowired private UserDetailsService scioUserDetailsService;
  /**
   * password encoder
   *
   * @return
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  /**
   * RememberMeAuthenticationProvider.
   *
   * @return
   */
  @Bean
  public RememberMeAuthenticationProvider rememberMeAuthenticationProvider() {
    return new RememberMeAuthenticationProvider(SECRET);
  }

  /**
   * TokenBasedRememberMeServices.
   *
   * @return
   */
  @Bean("tokenBaseRememberMeServices")
  public TokenBasedRememberMeServices tokenBasedRememberMeServices() {
    TokenBasedRememberMeServices rememberMeServices =
        new TokenBasedRememberMeServices(SECRET, scioUserDetailsService);
    rememberMeServices.setAlwaysRemember(false);
    rememberMeServices.setCookieName("remember-me");
    rememberMeServices.setTokenValiditySeconds(AbstractRememberMeServices.TWO_WEEKS_S);
    return rememberMeServices;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(scioUserDetailsService).passwordEncoder(passwordEncoder());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // http.antMatcher("/api/**");
    // .antMatchers("/api/login", "/api/logout")
    http.csrf()
        .disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
    // permit login
    http.formLogin()
        .permitAll()
        .defaultSuccessUrl("/info")
        .and()
        .authorizeRequests()
        .anyRequest()
        .authenticated();
    http.rememberMe()
        // tokenBased, store token and user name in token data.
        .rememberMeServices(tokenBasedRememberMeServices())
        // .tokenRepository(new InMemoryTokenRepositoryImpl())
        // persistent serials/token, invalidate after delete from token store
        .and()
        .authenticationProvider(rememberMeAuthenticationProvider());
    http.headers().cacheControl();
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/favicon.ico", "/**.js", "/**.css");
  }
}
