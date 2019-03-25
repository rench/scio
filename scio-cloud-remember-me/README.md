## SCIO
[![CircleCI](https://circleci.com/gh/rench/scio.svg?style=svg)](https://circleci.com/gh/rench/scio)
[![Auth](https://img.shields.io/badge/Author-Wang.ch-blue.svg)](https://xuankejia.cn)
[![GitHub stars](https://img.shields.io/github/stars/rench/scio.svg?style=social&label=Stars)](https://github.com/rench/scio)
[![GitHub forks](https://img.shields.io/github/forks/rench/scio.svg?style=social&label=Fork)](https://github.com/rench/scio)

----

# Remember Me [中文]()
## Introduction
* The Spring Security framework provides a token-based function to remember me. Generally, remember that my function is mainly used after the user enters the password for the first time in the current browser, and can enter the password for a while. You can access protected data endpoints even if the session fails.
## Implemention
* Remember that my implementation is based on the browser's persistence function for cookies. When the user first checks to remember me to log in, the server constructs an encrypted token based on the current user login information. The implementation of the token in Spring is mainly Two kinds:
1. **TokenBasedRememberMeServices** Generate a token containing the username in the cookie. Each time the request gets the username from the brought cookie, then the token is obtained from the userDetailsService according to the username. The way to generate is as follows:
```
username + ":" + expiryTime + ":"
        + Md5Hex(username + ":" + expiryTime + ":" + password + ":" + key)
```
2. **PersistentTokenBasedRememberMeServices** The generated cookie does not contain the username, contains the series and token, and is stored in the database. At the same time, the stored data also contains the user name and the most recent access time of the token. Each request is based on the series and tokens brought into the cookie, querying the data in the database for token comparison, and calling userDetailsService to check the status of the user.
> **PersistentTokenRepository** ，**PersistentTokenBasedRememberMeServices**Medium storage implementation. The default is InMemoryTokenRepositoryImpl, and the `JdbcTokenRepositoryImpl` implementation is included in the framework. It can be added according to your actual situation, such as redis.

3. Based on `TokenBasedRememberMeServices`, you can expand, encrypt the cookie, etc. The default cookie (`mp1:1554694664576:801c3f24109a468dfbeb7cf09cac0531`) does not have an encrypted username, but only base64 encoding, there may be a risk of leakage.

## Configuration
* SecurityConfig
```
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ScioRememberMeSecurityConfig extends WebSecurityConfigurerAdapter {
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
}
```

