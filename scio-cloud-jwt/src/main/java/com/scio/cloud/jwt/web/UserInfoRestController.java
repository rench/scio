package com.scio.cloud.jwt.web;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scio.cloud.jwt.web.vo.QueryVo;
/**
 * UserInfoRestController
 *
 * @author Wang.ch
 * @date 2019-03-27 18:32:10
 */
@RestController
public class UserInfoRestController {
  /**
   * get current user info
   *
   * @param principal
   * @return
   */
  @RequestMapping("/info")
  public String info(Principal principal) {
    return principal.getName();
  }

  @RequestMapping("/query")
  public QueryVo query(@RequestBody QueryVo query) {
    return query;
  }
}
