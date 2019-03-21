package com.scio.cloud.oauth2.resource;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * user info controller for oauth2 check
 *
 * @author Wang.ch
 * @date 2019-03-20 18:27:52
 */
@RestController
public class UserInfoRestController {

  @RequestMapping("/userinfo")
  public Principal userinfo(Principal principal) {
    return principal;
  }
}
