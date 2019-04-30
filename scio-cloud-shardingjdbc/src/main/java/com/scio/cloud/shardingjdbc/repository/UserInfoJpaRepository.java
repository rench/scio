package com.scio.cloud.shardingjdbc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scio.cloud.shardingjdbc.domain.UserInfo;
/**
 * user info jpa repository
 *
 * @author Wang.ch
 * @date 2019-04-15 09:56:08
 */
@Repository
public interface UserInfoJpaRepository
    extends JpaRepository<UserInfo, String>, UserInfoJpaRepositoryCustom {
  /**
   * find user info by user name
   *
   * @param username
   * @return
   */
  Optional<UserInfo> findByName(String name);
}
