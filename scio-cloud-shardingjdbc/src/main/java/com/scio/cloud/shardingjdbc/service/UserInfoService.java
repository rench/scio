package com.scio.cloud.shardingjdbc.service;

import java.util.List;

import com.scio.cloud.shardingjdbc.web.vo.UserInfoVo;

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
  UserInfoVo findById(String id);
  /**
   * find user info by user name
   *
   * @param name
   * @return
   */
  UserInfoVo findByName(String name);
}
