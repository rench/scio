package com.scio.cloud.oauth2.resource.web;

import java.security.Principal;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * @author Wang.ch
 * @date 2019-03-20 18:30:53
 */
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
