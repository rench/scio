package com.scio.cloud.shardingjdbc.repository;

import java.util.List;

import com.scio.cloud.shardingjdbc.domain.UserInfo;
/**
 * user info jpa repository custom
 *
 * @author Wang.ch
 * @date 2019-04-15 09:46:13
 */
public interface UserInfoJpaRepositoryCustom {
  /**
   * batch save user info
   *
   * @param userinfos
   * @return
   */
  List<UserInfo> batchSave(List<UserInfo> userinfos);
}
