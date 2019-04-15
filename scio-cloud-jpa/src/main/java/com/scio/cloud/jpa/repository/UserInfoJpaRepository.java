package com.scio.cloud.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scio.cloud.jpa.domain.UserInfo;
/**
 * user info jpa repository
 *
 * @author Wang.ch
 * @date 2019-04-15 09:56:08
 */
@Repository
public interface UserInfoJpaRepository
    extends JpaRepository<UserInfo, Long>, UserInfoJpaRepositoryCustom {
  /**
   * find user info by username
   *
   * @param username
   * @return
   */
  Optional<UserInfo> findByUsername(String username);
}
