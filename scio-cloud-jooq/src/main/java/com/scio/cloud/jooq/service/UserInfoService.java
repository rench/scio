package com.scio.cloud.jooq.service;

import java.util.List;

import com.scio.cloud.jooq.web.vo.UserInfoVo;

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
   * save or update user info
   *
   * @param vo
   * @return
   */
  List<UserInfoVo> save(List<UserInfoVo> userinfos);
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
  /**
   * find all user
   *
   * @return
   */
  List<UserInfoVo> findAll();
  /**
   * update
   *
   * @param id
   * @return
   */
  UserInfoVo update(Long id);
}
