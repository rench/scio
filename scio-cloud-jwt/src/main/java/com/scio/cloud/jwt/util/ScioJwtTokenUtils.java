package com.scio.cloud.jwt.util;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

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
