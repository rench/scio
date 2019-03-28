## SCIO
[![CircleCI](https://circleci.com/gh/rench/scio.svg?style=svg)](https://circleci.com/gh/rench/scio)
[![Auth](https://img.shields.io/badge/Author-Wang.ch-blue.svg)](https://xuankejia.cn)
[![GitHub stars](https://img.shields.io/github/stars/rench/scio.svg?style=social&label=Stars)](https://github.com/rench/scio)
[![GitHub forks](https://img.shields.io/github/forks/rench/scio.svg?style=social&label=Fork)](https://github.com/rench/scio)
-----
# scio-cloud-jwt
> https://github.com/rench/scio/tree/master/scio-cloud-jwt
## Jwt - JSON Web Token
### 原理
- http://www.ruanyifeng.com/blog/2018/07/json_web_token-tutorial.html
- JWT 的原理是，服务器认证以后，生成一个 JSON 对象，发回给用户，以后，用户与服务端通信的时候，都要发回这个 JSON 对象。服务器完全只靠这个对象认定用户身份。为了防止用户篡改数据，服务器在生成这个对象的时候，会加上签名，服务器就不保存任何 session 数据了，也就是说，服务器变成无状态了，从而比较容易实现扩展。

### 数据结构
> eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiVVNFUiIsImlzcyI6IldhbmcuY2giLCJzdWIiOiJtcDEiLCJpYXQiOjE1NTM2ODI1MTUsImV4cCI6MTU1MzY4NjExNX0.0FqNKPtk0fWscm9PopEEZ9ibiA1EFDz-uudTbAx_gQLWVKB3ifDFTVi8rTkd3UF6LCDaLl_kvZnPKbo-Rm0aYA
- JWT的数据结构是一个很长的字符串，中间用点（.）分隔成三个部分。注意，JWT 内部是没有换行的，这里只是为了便于展示，将它写成了几行。
- JWT 的三个部分，**Header（头部）**， **Payload（负载）**，**Signature（签名）**。
1) **Header** 部分是一个 JSON 对象，描述 JWT 的元数据，包含两个字段：alg，typ。
2) **Payload** Payload 部分也是一个 JSON 对象，用来存放实际需要传递的数据。JWT 规定了7个官方字段，供选用。
> - iss (issuer)：签发人
> - exp (expiration time)：过期时间
> - sub (subject)：主题
> - aud (audience)：受众
> - nbf (Not Before)：生效时间
> - iat (Issued At)：签发时间
> - jti (JWT ID)：编号
> - 除了以上字段外，还可以自己添加字段，本质就是一个map
3) **Signature** 部分是对前两部分的签名，防止数据篡改。首先，需要指定一个密钥（secret）。这个密钥只有服务器才知道，不能泄露给用户。然后，使用 Header 里面指定的签名算法（默认是 HMAC SHA256），按照下面的公式产生签名。
```
HMACSHA256(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  secret)
```
> 算出签名以后，把 Header、Payload、Signature 三个部分拼成一个字符串，每个部分之间用"点"（.）分隔，就可以返回给用户。
4) **Base64URL** Header 和 Payload 串型化的算法是 Base64URL。这个算法跟 Base64 算法基本类似，但有一些小的不同。
> JWT 作为一个令牌（token），有些场合可能会放到 URL（比如 api.scio.com/api/info?token=xxx）。Base64 有三个字符+、/和=，在 URL 里面有特殊含义，所以要被替换掉：=被省略、+替换成-，/替换成_ 。这就是 Base64URL 算法。

### Spring Boot JWT
#### 配置Spring Security和JWT
- 在产生JWT的时候，主要点是在用户名和密码验证通过后，获取验证成功的结果，将用户名+权限等信息用JWT进行加密产生token。
- 在使用JWT的时候，主要点是在没有身份认证成功之前，通过获取指定的Header中的token解析出用户名和权限，同时对过期时间进行校验，构造授权认证结果，并放入*SecurityContextHolder*中。
- **ScioWebSecurityConfig**
```
/**
   * web security config
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
      ScioJwtAuthorizationFilter authorizationFilter = new ScioJwtAuthorizationFilter();
      authorizationFilter.setAuthenticationManager(authenticationManagerBean());
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
          .addFilter(authorizationFilter)
          .addFilter(new ScioJwtAuthenticationFilter(authenticationManagerBean()));
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
```
- **ScioJwtAuthorizationFilter**
```
/**
 * JwtAuthorizationFilter to create authorization token
 *
 * <pre>
 * public UsernamePasswordAuthenticationFilter() {
 *   super(new AntPathRequestMatcher("/login", "POST"));
 * }
 * </pre>
 *
 * @see UsernamePasswordAuthenticationFilter
 * @author Wang.ch
 * @date 2019-03-27 17:23:37
 */
public class ScioJwtAuthorizationFilter extends UsernamePasswordAuthenticationFilter {
  /** successful authentication then create and return a token */
  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult)
      throws IOException, ServletException {
    SecurityContextHolder.getContext().setAuthentication(authResult);
    User user = (User) authResult.getPrincipal();
    String token = ScioJwtTokenUtils.createToken(user.getUsername(), user.getAuthorities(), false);
    response.setHeader(ScioJwtTokenUtils.TOKEN_HEADER, token);
  }
}
```
- **ScioJwtAuthenticationFilter**
```
/**
 * JwtAuthenticationFilter
 *
 * @author Wang.ch
 * @date 2019-03-27 18:12:37
 */
public class ScioJwtAuthenticationFilter extends BasicAuthenticationFilter {

  public ScioJwtAuthenticationFilter(AuthenticationManager authenticationManager) {
    super(authenticationManager);
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    String token = request.getHeader(ScioJwtTokenUtils.TOKEN_HEADER);
    if (token == null || !token.startsWith(ScioJwtTokenUtils.TOKEN_PREFIX)) {
      chain.doFilter(request, response);
      return;
    }
    UsernamePasswordAuthenticationToken authentication = retrieveAuthentication(token);
    if (authentication != null) {
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    super.doFilterInternal(request, response, chain);
  }
  /**
   * retrieve eAuthentication
   *
   * @param token
   * @return
   */
  private UsernamePasswordAuthenticationToken retrieveAuthentication(String token) {
    token = token.replace(ScioJwtTokenUtils.TOKEN_PREFIX, "");
    String username = ScioJwtTokenUtils.getUserName(token);
    String role = ScioJwtTokenUtils.getRoles(token);
    List<SimpleGrantedAuthority> roleList =
        Stream.of(Optional.ofNullable(role).orElse("").split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    if (username != null) {
      return new UsernamePasswordAuthenticationToken(username, null, roleList);
    }
    return null;
  }
}
```
- **ScioJwtTokenUtils**
```
/**
 * jwt token build utils
 *
 * @author Wang.ch
 * @date 2019-03-27 17:26:21
 */
public final class ScioJwtTokenUtils {
  // header
  public static final String TOKEN_HEADER = "Authorization";
  public static final String TOKEN_PREFIX = "Bearer ";
  // secret
  private static final String SECRET = "scio-jwt-secret";
  private static final String ISSUER = "Wang.ch";
  private static final String ROLE = "role";
  // one hour
  private static final long EXPIRATION = 3600L;
  // one week after check remember
  private static final long EXPIRATION_REMEMBER = 604800L;
  /**
   * create token with provider parameters
   *
   * @param username
   * @param roles
   * @param isRememberMe
   * @return
   */
  public static String createToken(
      String username, Collection<GrantedAuthority> roles, boolean isRememberMe) {
    long expiration = isRememberMe ? EXPIRATION_REMEMBER : EXPIRATION;
    String role =
        roles.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
    return Jwts.builder()
        .signWith(SignatureAlgorithm.HS512, SECRET)
        .claim(ROLE, role)
        .setIssuer(ISSUER)
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
        .compact();
  }
  /**
   * get user name from token
   *
   * @param token
   * @return
   */
  public static String getUserName(String token) {
    return getTokenClaims(token).getSubject();
  }
  /**
   * get roles from token
   *
   * @param token
   * @return
   */
  public static String getRoles(String token) {
    return getTokenClaims(token).get(ROLE, String.class);
  }
  /**
   * get claims from token
   *
   * @param token
   * @return
   */
  public static Claims getTokenClaims(String token) {
    return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
  }
}
```

#### 测试
- 获取token
```
curl -X POST \
  http://localhost:8007/login \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  -d 'username=mp1&password=mp1'
```
在header中会返回：
```
Authorization →eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiVVNFUiIsImlzcyI6IldhbmcuY2giLCJzdWIiOiJtcDEiLCJpYXQiOjE1NTM2ODI1MTUsImV4cCI6MTU1MzY4NjExNX0.0FqNKPtk0fWscm9PopEEZ9ibiA1EFDz-uudTbAx_gQLWVKB3ifDFTVi8rTkd3UF6LCDaLl_kvZnPKbo-Rm0aYA
```
- 使用token
```
curl -X GET \
  http://localhost:8007/info \
  -H 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiVVNFUiIsImlzcyI6IldhbmcuY2giLCJzdWIiOiJtcDEiLCJpYXQiOjE1NTM2ODI1MTUsImV4cCI6MTU1MzY4NjExNX0.0FqNKPtk0fWscm9PopEEZ9ibiA1EFDz-uudTbAx_gQLWVKB3ifDFTVi8rTkd3UF6LCDaLl_kvZnPKbo-Rm0aYA' \
  -H 'Cache-Control: no-cache' 
```
