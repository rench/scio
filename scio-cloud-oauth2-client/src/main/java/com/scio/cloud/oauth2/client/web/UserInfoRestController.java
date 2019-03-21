package com.scio.cloud.oauth2.client.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * @author Wang.ch
 * @date 2019-03-20 18:30:53
 */
@RestController
public class UserInfoRestController {
  @Autowired OAuth2RestTemplate rest;
  /**
   * access with oauth2 token to get access token userinfo
   *
   * @param principal
   * @return
   */
  @RequestMapping("/userinfo")
  public String userinfo() {
    return rest.getForEntity("http://localhost:8004/userinfo", String.class).getBody();
  }
}
