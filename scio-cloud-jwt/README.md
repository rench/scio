## SCIO
[![CircleCI](https://circleci.com/gh/rench/scio.svg?style=svg)](https://circleci.com/gh/rench/scio)
[![Auth](https://img.shields.io/badge/Author-Wang.ch-blue.svg)](https://xuankejia.cn)
[![GitHub stars](https://img.shields.io/github/stars/rench/scio.svg?style=social&label=Stars)](https://github.com/rench/scio)
[![GitHub forks](https://img.shields.io/github/forks/rench/scio.svg?style=social&label=Fork)](https://github.com/rench/scio)
-----
# scio-cloud-jwt [中文](https://github.com/rench/scio/tree/master/scio-cloud-jwt/README_zh.md)
> https://github.com/rench/scio/tree/master/scio-cloud-jwt
## Jwt - JSON Web Token
### Principle
- http://www.ruanyifeng.com/blog/2018/07/json_web_token-tutorial.html
- The principle of JWT is that after the server is authenticated, a JSON object is generated and sent back to the user. Later, when the user communicates with the server, the JSON object is sent back. The server relies solely on this object to identify the user. In order to prevent users from tampering with the data, the server will add a signature when generating the object, and the server will not save any session data, that is, the server becomes stateless, which makes it easier to extend.

### Data structure
> eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiVVNFUiIsImlzcyI6IldhbmcuY2giLCJzdWIiOiJtcDEiLCJpYXQiOjE1NTM2ODI1MTUsImV4cCI6MTU1MzY4NjExNX0.0FqNKPtk0fWscm9PopEEZ9ibiA1EFDz-uudTbAx_gQLWVKB3ifDFTVi8rTkd3UF6LCDaLl_kvZnPKbo-Rm0aYA
- The JWT data structure is a very long string separated by a dot (.) into three parts. Note that there is no line break inside the JWT, just for the sake of display, write it into a few lines.
- Three parts of JWT, **Header**, **Payload**, **Signature**.
1) The **Header** section is a JSON object that describes the metadata of the JWT and contains two fields: alg, typ.
2) **Payload** The Payload section is also a JSON object that holds the data that needs to be passed. The JWT specifies seven official fields for selection.
> - iss (issuer): issuer
> - exp (expiration time): expiration time
> - sub (subject): topic
> - aud (audience): Audience
> - nbf (Not Before): Effective time
> - iat (Issued At): Issue time
> - jti (JWT ID): number
> - In addition to the above fields, you can also add fields yourself, the essence is a map
3) The **Signature** section is a signature of the first two sections to prevent data tampering. First, you need to specify a secret (secret). This key is known only to the server and cannot be disclosed to the user. Then, using the signature algorithm specified in the Header (default is HMAC SHA256), generate the signature according to the following formula.
```
HMACSHA256(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  secret)
```
> After calculating the signature, the header, Payload, and Signature are combined into a string, and each part is separated by "dot" (.), and can be returned to the user.
4) **Base64URL** The algorithm for header and Payload serialization is Base64URL. This algorithm is basically similar to the Base64 algorithm, but with some minor differences.
> JWT as a token, some occasions may be placed in a URL (such as api.scio.com/api/info?token=xxx). Base64 has three characters +, / and =, which have special meanings in the URL, so they are replaced: = omitted, + replaced with -, / replaced with _. This is the Base64URL algorithm.

### Spring Boot JWT
#### Configuring Spring Security and JWT
- When generating JWT, the main point is to obtain the result of successful verification after the user name and password are verified, and the user name + authority and other information are encrypted by JWT to generate a token.
- When using JWT, the main point is to resolve the user name and permissions by obtaining the token in the specified header before the identity authentication succeeds, and verify the expiration time, construct the authorization authentication result, and put it into *SecurityContextHolder* in.
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

#### Test
- Get token
```
curl -X POST \
  http://localhost:8007/login \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  -d 'username=mp1&password=mp1'
```
Will return in the header:
```
Authorization →eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiVVNFUiIsImlzcyI6IldhbmcuY2giLCJzdWIiOiJtcDEiLCJpYXQiOjE1NTM2ODI1MTUsImV4cCI6MTU1MzY4NjExNX0.0FqNKPtk0fWscm9PopEEZ9ibiA1EFDz-uudTbAx_gQLWVKB3ifDFTVi8rTkd3UF6LCDaLl_kvZnPKbo-Rm0aYA
```
- Use token
```
curl -X GET \
  http://localhost:8007/info \
  -H 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiVVNFUiIsImlzcyI6IldhbmcuY2giLCJzdWIiOiJtcDEiLCJpYXQiOjE1NTM2ODI1MTUsImV4cCI6MTU1MzY4NjExNX0.0FqNKPtk0fWscm9PopEEZ9ibiA1EFDz-uudTbAx_gQLWVKB3ifDFTVi8rTkd3UF6LCDaLl_kvZnPKbo-Rm0aYA' \
  -H 'Cache-Control: no-cache' 
```
