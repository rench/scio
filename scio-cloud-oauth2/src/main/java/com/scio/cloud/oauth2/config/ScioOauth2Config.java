package com.scio.cloud.oauth2.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
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
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.google.common.collect.Maps;
/**
 * https://github.com/spring-projects/spring-security-oauth/issues/214
 *
 * @author Wang.ch
 * @date 2019-03-19 15:38:33
 */
@SuppressWarnings("deprecation")
@Configuration
public class ScioOauth2Config {

  /**
   * redis token store
   *
   * @return
   */
  @Bean
  @ConditionalOnProperty(prefix = "security.oauth2", name = "storeType", havingValue = "redis")
  public TokenStore redisTokenStore() {
    // JdbcTokenStore,RedisTokenStore
    // return new RedisTokenStore(null);
    return null;
  }

  /**
   * store oauth2 token in memory
   *
   * @return
   */
  @Bean
  @ConditionalOnProperty(prefix = "security.oauth2", name = "storeType", havingValue = "memory")
  public TokenStore inMemoryTokenStore() {
    return new InMemoryTokenStore();
  }
  /**
   * jwt token store
   *
   * @return
   */
  @Bean
  @ConditionalOnProperty(prefix = "security.oauth2", name = "storeType", havingValue = "jwt")
  public TokenStore jwtTokenStore() {
    return new JwtTokenStore(converter());
  }
  /**
   * jwt token convert
   *
   * @return
   */
  @Bean
  @ConditionalOnProperty(prefix = "security.oauth2", name = "storeType", havingValue = "jwt")
  public JwtAccessTokenConverter converter() {
    JwtAccessTokenConverter convert = new JwtAccessTokenConverter();
    // curl client1:123456@http://localhost:8003/oauth/token_key
    // convert.setSigningKey("scio@2019");
    try {
      String rsaContent =
          FileCopyUtils.copyToString(
              new BufferedReader(
                  new InputStreamReader(
                      new ClassPathResource("scio.jwt.private.key").getInputStream())));
      convert.setSigningKey(rsaContent);
      rsaContent =
          FileCopyUtils.copyToString(
              new BufferedReader(
                  new InputStreamReader(
                      new ClassPathResource("scio.jwt.public.key").getInputStream())));
      convert.setVerifierKey(rsaContent);
    } catch (IOException e) {
      e.printStackTrace();
      convert.setSigningKey("scio@2019");
    }
    // KeyPair kp = new KeyStoreKeyFactory(new ClassPathResource("scio.jwt.jks"),
    // null).getKeyPair("");
    //    convert.setKeyPair(kp);
    return convert;
  }
  /**
   * mock resource owners
   *
   * @author Wang.ch
   * @date 2019-03-21 08:13:53
   */
  @Service
  public static class ScioUserDetailsService implements UserDetailsService {
    /** mmock users */
    private Map<String, String> users = Maps.newHashMap();

    public ScioUserDetailsService() {
      users.put("mp1", "mp1");
      users.put("mp2", "mp2");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      if (users.containsKey(username)) {
        String noopPwd = users.get(username);
        User u = new User(username, noopPwd, Arrays.asList(new SimpleGrantedAuthority("USER")));
        return u;
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
      // auth.inMemoryAuthentication().withUser("mp").password("{noop}123456").roles("USER");
      auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      // Define which links require user login privileges
      http.requestMatchers()
          .antMatchers("/login", "/oauth/authorize")
          .and()
          .authorizeRequests()
          .anyRequest()
          .authenticated()
          .and()
          .formLogin()
          .permitAll();
      http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
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
  /**
   * AuthorizationServerConfiguration
   *
   * @doc https://www.cnblogs.com/xingxueliao/p/5911292.html
   * @author Wang.ch
   * @date 2019-03-19 16:28:55
   */
  @Configuration
  @EnableAuthorizationServer
  @Order(Ordered.LOWEST_PRECEDENCE)
  public static class ScioAuthorizationServerConfiguration
      extends AuthorizationServerConfigurerAdapter {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private TokenStore tokenStore;

    @Autowired(required = false)
    private JwtAccessTokenConverter converter;

    @Autowired private UserDetailsService userDetailsService;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
      endpoints.pathMapping("/oauth/token", "/oauth/token");

      // /oauth/authorize：Authorized endpoint
      // /oauth/token：Token endpoint
      // /oauth/confirm_access：User confirms authorization to submit endpoint
      // /oauth/error：Authorization service error message endpoint
      // /oauth/check_token：Token resolution endpoint for resource service access
      // /oauth/token_key：Provide the endpoint of the public key, if you use a JWT token

      endpoints.tokenStore(tokenStore).authenticationManager(authenticationManager);
      // DefaultTokenServices.refreshAccessToken need
      endpoints.userDetailsService(userDetailsService);
      TokenEnhancerChain chain = new TokenEnhancerChain();
      // for custom token
      TokenEnhancer enhancer = new ScioOauth2TokenEnhancer();
      List<TokenEnhancer> list = new ArrayList<>(2);
      list.add(enhancer);
      if (converter != null) {
        list.add(converter);
        // add convert
        endpoints.accessTokenConverter(converter);
      }
      chain.setTokenEnhancers(list);
      endpoints.tokenEnhancer(chain);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
      // enable client to get the authenticated when using the /oauth/token to get a access token
      // there is a 401 authentication is required if it doesn't allow form authentication for
      // clients when access /oauth/token
      security.allowFormAuthenticationForClients();
      security.tokenKeyAccess("permitAll()");
      security.checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
      // refresh_token only allowed with GrantType:authorization_code,password
      clients
          .inMemory()
          .withClient("client1")
          .scopes("read", "write")
          .secret(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("123456"))
          .authorizedGrantTypes(
              "authorization_code", "refresh_token", "implicit", "password", "client_credentials")
          .redirectUris("http://www.baidu.com")
          .and()
          .withClient("scio-cloud-oauth2-client")
          .scopes("read", "write")
          .secret(
              PasswordEncoderFactories.createDelegatingPasswordEncoder()
                  .encode("48f854f3cee94afba7ae95a6c5ce9116"))
          .authorizedGrantTypes("client_credentials");
    }
  }
}
