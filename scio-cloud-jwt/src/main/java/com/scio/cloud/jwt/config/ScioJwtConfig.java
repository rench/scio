package com.scio.cloud.jwt.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
/**
 * https://github.com/spring-projects/spring-security-oauth/issues/214
 *
 * @author Wang.ch
 * @date 2019-03-19 15:38:33
 */
@SuppressWarnings("deprecation")
@Configuration
public class ScioJwtConfig {
  /**
   * mock resource owners
   *
   * @author Wang.ch
   * @date 2019-03-21 08:13:53
   */
  @Service
  public static class ScioUserDetailsService implements UserDetailsService {
    /** mock users */
    private Map<String, String> users = new HashMap<>(2);

    public ScioUserDetailsService() {
      users.put("mp1", "mp1");
      users.put("mp2", "mp2");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      if (users.containsKey(username)) {
        String noopPwd = users.get(username);
        User user = new User(username, noopPwd, Arrays.asList(new SimpleGrantedAuthority("USER")));
        return user;
      } else {
        throw new UsernameNotFoundException("user not found");
      }
    }
  }

  /**
   * resource owner web security before oauth2 authentication
   *
   * @doc https://docs.spring.io/spring-security/site/docs/current/guides/html5/helloworld-boot.html
   * @doc https://github.com/shimingda/security
   * @author Wang.ch
   * @date 2019-03-20 10:12:06
   */
  @Configuration
  @EnableWebSecurity
  @EnableGlobalMethodSecurity(prePostEnabled = true)
  @Order(1)
  public static class ScioWebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
      DelegatingPasswordEncoder delegate =
          (DelegatingPasswordEncoder) PasswordEncoderFactories.createDelegatingPasswordEncoder();
      delegate.setDefaultPasswordEncoderForMatches(NoOpPasswordEncoder.getInstance());
      return delegate;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      // Define which links require user login privileges
      JwtAuthenticationFilter f = new JwtAuthenticationFilter();
      f.setAuthenticationManager(authenticationManager());
      http.requestMatchers()
          .antMatchers("/login", "/info")
          .and()
          .authorizeRequests()
          .antMatchers("/login")
          .permitAll()
          .anyRequest()
          .authenticated()
          .and()
          .formLogin()
          .disable()
          .csrf()
          .disable()
          .addFilter(f)
          .addFilter(new JwtAuthorizationFilter(authenticationManagerBean()));

      http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
      return super.authenticationManager();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
      return super.authenticationManagerBean();
    }
  }
}
