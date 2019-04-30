package com.scio.cloud.shardingjdbc.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.scio.cloud.shardingjdbc.domain.UserInfo;
/**
 * user info custom implementation
 *
 * @author Wang.ch
 * @date 2019-04-15 09:48:38
 */
public class UserInfoJpaRepositoryImpl implements UserInfoJpaRepositoryCustom {

  @Autowired private EntityManager entityManager;

  @Override
  @Transactional
  public List<UserInfo> batchSave(List<UserInfo> userinfos) {
    userinfos.stream().forEach(entityManager::persist);
    return userinfos;
  }
}
