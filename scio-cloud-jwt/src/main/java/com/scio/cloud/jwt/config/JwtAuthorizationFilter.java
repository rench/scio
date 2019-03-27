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

import com.scio.cloud.jwt.util.JwtTokenUtils;
/**
 * jwt auto auth filter
 *
 * @author Wang.ch
 * @date 2019-03-27 18:12:37
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

  public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
    super(authenticationManager);
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    String token = request.getHeader(JwtTokenUtils.TOKEN_HEADER);
    if (token == null || !token.startsWith(JwtTokenUtils.TOKEN_PREFIX)) {
      chain.doFilter(request, response);
      return;
    }
    UsernamePasswordAuthenticationToken authentication = getAuthentication(token);
    if (authentication != null) {
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    super.doFilterInternal(request, response, chain);
  }

  private UsernamePasswordAuthenticationToken getAuthentication(String token) {
    token = token.replace(JwtTokenUtils.TOKEN_PREFIX, "");
    String username = JwtTokenUtils.getUserName(token);
    String role = JwtTokenUtils.getRoles(token);
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
