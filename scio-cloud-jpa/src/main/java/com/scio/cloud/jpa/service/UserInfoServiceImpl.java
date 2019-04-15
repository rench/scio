package com.scio.cloud.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scio.cloud.jpa.domain.UserInfo;
import com.scio.cloud.jpa.repository.UserInfoJpaRepository;
import com.scio.cloud.jpa.utils.BeanCopyUtils;
import com.scio.cloud.jpa.web.vo.UserInfoVo;
/**
 * user info service
 *
 * @author Wang.ch
 * @date 2019-04-15 10:29:47
 */
@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {
  @Autowired private UserInfoJpaRepository userinfo;

  @Override
  @Transactional
  public UserInfoVo save(UserInfoVo vo) {
    UserInfo save = BeanCopyUtils.copy(vo, UserInfo::new);
    userinfo.save(save);
    return BeanCopyUtils.copy(save, UserInfoVo::new);
  }

  @Override
  public UserInfoVo findById(Long id) {
    return userinfo
        .findById(id)
        .map(info -> BeanCopyUtils.copy(info, UserInfoVo::new))
        .orElse(null);
  }

  @Override
  public UserInfoVo findByUsername(String username) {
    return userinfo
        .findByUsername(username)
        .map(info -> BeanCopyUtils.copy(info, UserInfoVo::new))
        .orElse(null);
  }
}
