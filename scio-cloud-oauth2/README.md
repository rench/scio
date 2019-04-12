## SCIO
[![CircleCI](https://circleci.com/gh/rench/scio.svg?style=svg)](https://circleci.com/gh/rench/scio)
[![Auth](https://img.shields.io/badge/Author-Wang.ch-blue.svg)](https://xuankejia.cn)
[![GitHub stars](https://img.shields.io/github/stars/rench/scio.svg?style=social&label=Stars)](https://github.com/rench/scio)
[![GitHub forks](https://img.shields.io/github/forks/rench/scio.svg?style=social&label=Fork)](https://github.com/rench/scio)
-----
# scio-cloud-oauth2
> https://github.com/rench/scio/tree/master/scio-cloud-oauth2
# OAuth 2.0
## OAuth 2.0定义
- 基于RFC6749翻译的中文版本 https://github.com/rench/RFC6749.zh-cn
- 中文翻译 http://www.udpwork.com/item/16243.html
## OAuth 2.0的角色定义
- 资源所有者 **Resource Owner**
- 资源服务器 **Resource Server**
- 客户端     **OAuth 2.0 Client**
- 授权服务器 **Authorization Server**
 
## OAuth 2.0协议流程

```
 +--------+                               +---------------+
 |        |--(A)- Authorization Request ->|   Resource    |
 |        |                               |     Owner     |
 |        |<-(B)-- Authorization Grant ---|               |
 |        |                               +---------------+
 |        |
 |        |                               +---------------+
 |        |--(C)-- Authorization Grant -->| Authorization |
 | Client |                               |     Server    |
 |        |<-(D)----- Access Token -------|               |
 |        |                               +---------------+
 |        |
 |        |                               +---------------+
 |        |--(E)----- Access Token ------>|    Resource   |
 |        |                               |     Server    |
 |        |<-(F)--- Protected Resource ---|               |
 +--------+                               +---------------+
 ```
- （A）客户端向从资源所有者请求授权。授权请求可以直接向资源所有者发起（如图所示），或者更可取的是通过作为中介的授权服务器间接发起。
- （B）客户端收到授权许可，这是一个代表资源所有者的授权的凭据，使用本规范中定义的四种许可类型之一或 者使用扩展许可类型表示。授权许可类型取决于客户端请求授权所使用的方式以及授权服务器支持的类型。
- （C）客户端与授权服务器进行身份认证并出示授权许可请求访问令牌。
- （D）授权服务器验证客户端身份并验证授权许可，若有效则颁发访问令牌。
- （E）客户端从资源服务器请求受保护资源并出示访问令牌进行身份验证。
- （F）资源服务器验证访问令牌，若有效则满足该请求。

## OAuth 2.0授权许可
- 授权码 **authorization_code**
- 隐式授权 **implicit**
- 资源所有者密码凭据 **password**
- 客户端凭据 **client_credentials**

## OAuth 2.0令牌刷新
- 令牌刷新 **refresh_token**
```
+--------+                                           +---------------+
|        |--(A)------- Authorization Grant --------->|               |
|        |                                           |               |
|        |<-(B)----------- Access Token -------------|               |
|        |               & Refresh Token             |               |
|        |                                           |               |
|        |                            +----------+   |               |
|        |--(C)---- Access Token ---->|          |   |               |
|        |                            |          |   |               |
|        |<-(D)- Protected Resource --| Resource |   | Authorization |
| Client |                            |  Server  |   |     Server    |
|        |--(E)---- Access Token ---->|          |   |               |
|        |                            |          |   |               |
|        |<-(F)- Invalid Token Error -|          |   |               |
|        |                            +----------+   |               |
|        |                                           |               |
|        |--(G)----------- Refresh Token ----------->|               |
|        |                                           |               |
|        |<-(H)----------- Access Token -------------|               |
+--------+           & Optional Refresh Token        +---------------+
```

## OAuth 2.0常见例子
- **微博 authorization_code** https://open.weibo.com/wiki/%E6%8E%88%E6%9D%83%E6%9C%BA%E5%88%B6%E8%AF%B4%E6%98%8E?sudaref=www.baidu.com&display=0&retcode=6102
- **微信 client_credentials** https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140183

# Spring Boot&Cloud OAuth 2.0
> 本文结合Spring Boot OAuth 2和Spring Cloud OAuth 2进行示例，所有的资源和用户使用的是内存模拟数据，如在使用中，请替换为持久化存储。
## 解释
### 资源所有者 Resource Owner 
> 资源所有者即需要用户授权的用户，所有的资源请求，都需要在资源所有者授权后才可以进行资源授权。
### 资源服务器 Resource Server
> 资源服务器即资源所有者的资源存储的地方，一个客户端向资源服务器获取资源，必须要提供合法的授权token， 资源服务器根据token向授权服务器进行授权认证，认证合法后，判断该客户端有权读取指定的资源。
### 客户端 OAuth 2.0 Client
> 向资源服务器发起资源请求的客户端。
### 授权服务器 Authorization Server
> 客户端引导用户认证后，授权服务器会颁发合法的token给客户端，同时授权服务器也会提供token验证的功能等。

## 原理
### AuthorizationServer
1. Spring中的AuthorizationServer的原理是基于spring-security之上，`AuthorizationServer`进行`Filter`授权认证的必要条件是进行过`spring-security`的`Authentication`。
2. Spring中的AuthorizationServer可以支持多种客户端方式配置`ClientDetailsServiceConfigurer`，同时支持token的增强`TokenEnhancer`。
3. AuthorizationServer需要进行用户token验证，它也是一个`ResourceServer`。
### ResourceServer
1. ResourceServer的原理同样是基于`WebSecurity`配置的。在`ResourceServerConfigurerAdapter`中指定需要被保护的资源路径，`WebSecurity`会拦截到指定的请求，进行`OAuth2.0`的授权校验，授权成功后，填充`Authentication`。

## 使用
### AuthorizationServer
> https://github.com/rench/scio/tree/master/scio-cloud-oauth2
1. `AuthorizationServer`的配置，需要进入依赖`org.springframework.cloud:spring-cloud-starter-oauth2`，同时开启`@EnableAuthorizationServer`，并继承`AuthorizationServerConfigurerAdapter`进行配置.
```
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
      // for custom endpoints
      endpoints.pathMapping("/oauth/token", "/oauth/token");
      endpoints.tokenStore(tokenStore).authenticationManager(authenticationManager);
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
      endpoints.authorizationCodeServices(new ScioOauth2CodeServices());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
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
          .redirectUris("https://www.xuankejia.cn")
          .and()
          .withClient("scio-cloud-oauth2-client")
          .scopes("read", "write")
          .secret(
              PasswordEncoderFactories.createDelegatingPasswordEncoder()
                  .encode("48f854f3cee94afba7ae95a6c5ce9116"))
          .authorizedGrantTypes("client_credentials");
    }
  }
```

2. `Resource Onwer Authentication`需要配置`WebSecurityConfigurerAdapter`，来在授权服务器之前获取授权。
```
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
```
> 授权用户数据我们先进行内存模拟。
```
@Service
  public static class ScioUserDetailsService implements UserDetailsService {
    /** mock users */
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
```
3. 扩展授权服务器的code生成
```
public class ScioOauth2CodeServices implements AuthorizationCodeServices {
  // generator 4 char
  private RandomValueStringGenerator generator = new RandomValueStringGenerator(4);
  protected final ConcurrentHashMap<String, OAuth2Authentication> authorizationCodeStore =
      new ConcurrentHashMap<String, OAuth2Authentication>();

  @Override
  public String createAuthorizationCode(OAuth2Authentication authentication) {
    String code = generator.generate();
    authorizationCodeStore.put(code, authentication);

    return code;
  }

  @Override
  public OAuth2Authentication consumeAuthorizationCode(String code) throws InvalidGrantException {
    OAuth2Authentication auth = authorizationCodeStore.remove(code);
    if (auth == null) {
      throw new InvalidGrantException("Invalid authorization code: " + code);
    }
    return auth;
  }
}
```
4. 扩展授权服务器的token生成
```
public class ScioOauth2TokenEnhancer implements TokenEnhancer {

  @Override
  public OAuth2AccessToken enhance(
      OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
    if (accessToken instanceof DefaultOAuth2AccessToken) {
      DefaultOAuth2AccessToken enhancerToken = ((DefaultOAuth2AccessToken) accessToken);
      enhancerToken.setValue(getNewScioToken());
      OAuth2RefreshToken refreshToken = enhancerToken.getRefreshToken();
      if (refreshToken instanceof DefaultOAuth2RefreshToken) {
        enhancerToken.setRefreshToken(new DefaultOAuth2RefreshToken(getNewScioToken()));
      }
      Map<String, Object> additionalInformation = new HashMap<String, Object>();
      Map<String, Object> ext = new HashMap<>();
      Object principal = authentication.getPrincipal();
      if (principal instanceof User) {
        User user = (User) principal;
        ext.put("username", user.getUsername());
      } else {
        ext.put("username", principal);
      }
      ext.put("client_id", authentication.getOAuth2Request().getClientId());
      additionalInformation.put("ext", ext);
      enhancerToken.setAdditionalInformation(additionalInformation);
    }
    return accessToken;
  }

  private String getNewScioToken() {
    return "scio@" + UUID.randomUUID().toString().replace("-", "");
  }
}
```
5. 进行授权操作
- authorization_code
> 该模式下，需要用户在登录界面进行授权后，选择授权的scope。本例子送用户登录登录服务和授权服务在同一服务中，如果需要分离，可以进行授权服务器和登录服务器进行session共享。
```
1. 访问 http://localhost:8003/oauth/authorize?response_type=code&client_id=client1&redirect_uri=https://www.xuankejia.cn
2. 在登录页面输入模拟的用户名和密码
3. 选择授权的scope(read,write)
4. curl http://localhost:8003/oauth/token
        -dgrant_type=authorization_code
        -dclient_id=client1
        -dclient_secret=123456
        -dcode=ASp8Zb(替换为跳转的url中的token)
        -dredirect_uri=https://www.xuankejia.cn
5. 获取token信息
```
- refesh_token
> 在上一步拿到的token中包含的refresh_token参数，在token即将过期之前，可以使用refresh_token进行token刷新，获取新的token，有效期重新计算。
```
1. curl http://localhost:8003/oauth/token
       -dgrant_type=refresh_token
       -dclient_id=client1
       -dclient_secret=123456
       -drefresh_token=16ea4250-884f-4ca2-ac72-1c3d44550de0
2. 获取token信息
```
- password
> 该模式下，不需要用户授权，只需要提供client的用户名和密码和用户的账号和密码，即可获取授权，该模式主要在获取到用户的账号和密码后。
```
1. curl http://localhost:8003/oauth/token
      -dgrant_type=password
      -dclient_id=client1
      -dclient_secret=123456
      -dusername=mp1
      -dpassword=mp1
2. 获取token信息
```
- client_credentials
> 该模式下，不需要用户授权，只需要提供client的用户名和密码，即可获取授权，该模式主要用户获取针对client提供的授权服务以及身份认证。
```
1. curl http://localhost:8003/oauth/token
       -dgrant_type=client_credentials
       -dclient_id=client1
       -dclient_secret=123456
2. 获取token信息
```
### ResourceServer
> https://github.com/rench/scio/tree/master/scio-cloud-oauth2-resource
1. application.yml
```
security:
   oauth2:
      resource:
         userInfoUri: http://localhost:8003/userinfo
         token-info-uri: http://localhost:8003/oauth/check_token
         preferTokenInfo: false
```
2. ScioOauth2ResourceServerConfig
```
@Configuration
@EnableResourceServer
public class ScioOauth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

  @Override
  public void configure(HttpSecurity http) throws Exception {
    // define which resource will be protected by oauth2
    http.authorizeRequests().antMatchers("/userinfo").authenticated();
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    // UserInfoTokenServices to fetch user info from oauth2 server
  }
}
```
3. UserInfoRestController
```
@RestController
public class UserInfoRestController {
  /**
   * access with oauth2 token to get access token userinfo
   *
   * @param principal
   * @return
   */
  @RequestMapping("/userinfo")
  public String userinfo(Principal principal) {
    String username = null;
    if (principal instanceof OAuth2Authentication) {
      username = ((OAuth2Authentication) principal).getName();
    }
    return username;
  }
}
```

### Oauth2Client With Zuul
> https://github.com/rench/scio/tree/master/scio-cloud-oauth2-client
1. Oauth2ClientConfig
```
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

}
```
2. OAuth2ZuulFilter
```
@Component
@Configuration
@ConfigurationProperties(prefix = "security.oauth2")
public class OAuth2ZuulFilter extends ZuulFilter {
  private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
  private static final String TOKEN_TYPE = "TOKEN_TYPE";

  private List<String> zuulRoutes = new ArrayList<>();

  private OAuth2RestOperations restTemplate;
  @Autowired RouteLocator locator;

  @Autowired
  public void setRestTemplate(OAuth2RestOperations restTemplate) {
    // List<Route> list = locator.getRoutes();
    this.restTemplate = restTemplate;
  }

  @Override
  public int filterOrder() {
    return FilterConstants.PRE_DECORATION_FILTER_ORDER + 1;
  }

  @Override
  public String filterType() {
    return "pre";
  }

  @Override
  public boolean shouldFilter() {
    RequestContext ctx = RequestContext.getCurrentContext();
    if (ctx.containsKey("proxy")) {
      String id = (String) ctx.get("proxy");
      if (!zuulRoutes.contains(id)) {
        return false;
      } else {
        ctx.set(TOKEN_TYPE, "Bearer");
        return true;
      }
    }
    return false;
  }

  @Override
  public Object run() {
    RequestContext ctx = RequestContext.getCurrentContext();
    ctx.addZuulRequestHeader("authorization", ctx.get(TOKEN_TYPE) + " " + getAccessToken(ctx));
    return null;
  }

  private String getAccessToken(RequestContext ctx) {
    String value = (String) ctx.get(ACCESS_TOKEN);
    if (restTemplate != null) {
      try {
        value = restTemplate.getAccessToken().getValue();
      } catch (Exception e) {
        throw new BadCredentialsException("Cannot obtain valid access token");
      }
    }
    return value;
  }

  /** @param zuulRoutes the zuulRoutes to set */
  public void setZuulRoutes(List<String> zuulRoutes) {
    this.zuulRoutes = zuulRoutes;
  }
}
```
3. application.yml
```
security:
   basic:
      enabled: false
   oauth2:
      resource:
         userInfoUri: http://localhost:8003/userinfo
         token-info-uri: http://localhost:8003/oauth/check_token
         preferTokenInfo: false
      client:
         clientId: scio-cloud-oauth2-client
         clientSecret: 48f854f3cee94afba7ae95a6c5ce9116
         accessTokenUri: http://localhost:8003/oauth/token
         userAuthorizationUri: http://localhost:8003/oauth/authorize
         clientAuthenticationScheme: form
         authorized-grant-types: client_credentials
         grant-type: client_credentials
      zuul-routes:
         - scio-cloud-oauth2-resource
         
zuul:
  ignoredServices: '*'
  routes:
    scio-cloud-oauth2-resource:
      path: /res/**
      url: http://localhost:8004
      stripPrefix: true
```