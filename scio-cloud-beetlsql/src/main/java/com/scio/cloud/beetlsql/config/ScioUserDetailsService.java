package com.scio.cloud.beetlsql.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
/**
 * mock scio users
 *
 * @author Wang.ch
 * @date 2019-03-25 09:05:21
 */
@Configuration
public class ScioUserDetailsService implements UserDetailsService {
  /** mock users */
  private Map<String, String> users = new HashMap<>();

  public ScioUserDetailsService() {
    users.put("mp1", "{noop}mp1");
    users.put("mp2", "{noop}mp2");
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
