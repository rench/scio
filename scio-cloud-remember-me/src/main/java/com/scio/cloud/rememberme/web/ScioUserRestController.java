package com.scio.cloud.rememberme.web;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * form login home page
 *
 * @author Wang.ch
 * @date 2019-03-25 09:24:37
 */
@RestController
public class ScioUserRestController {
  @RequestMapping("/info")
  public String me(Principal principal) {
    return principal.getName();
  }
}
