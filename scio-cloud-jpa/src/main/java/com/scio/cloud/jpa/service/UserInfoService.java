package com.scio.cloud.jpa.service;

import com.scio.cloud.jpa.web.vo.UserInfoVo;

/**
 * user info service interface
 *
 * @author Wang.ch
 * @date 2019-04-15 10:27:02
 */
public interface UserInfoService {
  /**
   * save or update user info
   *
   * @param vo
   * @return
   */
  UserInfoVo save(UserInfoVo vo);
  /**
   * find user info by user id
   *
   * @param id
   * @return
   */
  UserInfoVo findById(Long id);
  /**
   * find user info by user name
   *
   * @param username
   * @return
   */
  UserInfoVo findByUsername(String username);
}
