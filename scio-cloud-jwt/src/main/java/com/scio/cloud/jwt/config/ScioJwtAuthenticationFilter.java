package com.scio.cloud.jwt.config;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.scio.cloud.jwt.util.ScioJwtTokenUtils;
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
