## SCIO
[![CircleCI](https://circleci.com/gh/rench/scio.svg?style=svg)](https://circleci.com/gh/rench/scio)
[![Auth](https://img.shields.io/badge/Author-Wang.ch-blue.svg)](https://xuankejia.cn)
[![GitHub stars](https://img.shields.io/github/stars/rench/scio.svg?style=social&label=Stars)](https://github.com/rench/scio)
[![GitHub forks](https://img.shields.io/github/forks/rench/scio.svg?style=social&label=Fork)](https://github.com/rench/scio)

----

# Remember Me 记住我
## 介绍
* Spring Security框架中提供了一种基于token的记住我的功能，一般的记住我功能主要用于用户在当前浏览器中首次输入密码后，即可在以后指定的一段时间内，不用输入密码即可访问受保护的数据短点，即使session失效。
## 实现
* 记住的我实现是基于浏览器对于cookie的持久化功能，在用户首次勾选记住我登录时，服务端基于当前用户登录信息构造一个加密后的token，该token在Spring中的实现主要有两种：
1. **TokenBasedRememberMeServices** 生成cookie中包含用户名的token，每次请求从带入的cookie中获取username，然后根据username从userDetailsService中获取数据生成token进行比较。生成的方式如下：
```
username + ":" + expiryTime + ":"
        + Md5Hex(username + ":" + expiryTime + ":" + password + ":" + key)
```
2. **PersistentTokenBasedRememberMeServices** 生成的cookie中不包含用户名，包含series和token，并存储于数据库中，同时，存储的数据中还包含了用户名和token最近访问时间。每次请求根据带入cookie中的series和token，查询数据库中的数据进行token比对，同时调用userDetailsService进行用户的状态校验。
> **PersistentTokenRepository** ，**PersistentTokenBasedRememberMeServices**中存储实现。默认是InMemoryTokenRepositoryImpl，框架中附带了`JdbcTokenRepositoryImpl`实现，可以根据自己的实际情况增加实现，比如redis等。

3. 基于`TokenBasedRememberMeServices`，可以进行扩展，对cookie进行加密等操作，默认的cookie(`mp1:1554694664576:801c3f24109a468dfbeb7cf09cac0531`)没有加密用户名，只是进行base64编码，可能存在泄漏的风险。

## 配置
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