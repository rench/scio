package com.scio.cloud.querydsl.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.scio.cloud.querydsl.domain.QUserInfo;
import com.scio.cloud.querydsl.domain.UserInfo;
import com.scio.cloud.querydsl.repository.UserInfoJpaRepository;
import com.scio.cloud.querydsl.utils.BeanCopyUtils;
import com.scio.cloud.querydsl.web.vo.UserInfoVo;
/**
 * user info service
 *
 * @author Wang.ch
 * @date 2019-04-15 10:29:47
 */
@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {
  @Autowired private UserInfoJpaRepository userinfo;
  @Autowired private JPAQueryFactory dsl;

  @Override
  @Transactional
  public UserInfoVo save(UserInfoVo vo) {
    UserInfo save = BeanCopyUtils.copy(vo, UserInfo::new);
    userinfo.save(save);
    return BeanCopyUtils.copy(save, UserInfoVo::new);
  }

  @Override
  @Transactional(readOnly = true)
  public UserInfoVo findById(Long id) {
    return userinfo
        .findById(id)
        .map(info -> BeanCopyUtils.copy(info, UserInfoVo::new))
        .orElse(null);
  }

  @Override
  @Transactional(readOnly = true)
  public UserInfoVo findByUsername(String username) {
    return userinfo
        .findByUsername(username)
        .map(info -> BeanCopyUtils.copy(info, UserInfoVo::new))
        .orElse(null);
  }

  @Override
  @Transactional
  public List<UserInfoVo> save(List<UserInfoVo> userinfos) {
    List<UserInfo> results =
        userinfo.batchSave(
            userinfos
                .stream()
                .map(info -> BeanCopyUtils.copy(info, UserInfo::new))
                .collect(Collectors.toList()));
    return results
        .stream()
        .map(info -> BeanCopyUtils.copy(info, UserInfoVo::new))
        .collect(Collectors.toList());
  }

  @Override
  public List<UserInfoVo> findAll() {
    List<UserInfo> list = dsl.selectFrom(QUserInfo.userInfo).fetchAll().fetch();
    return list.stream()
        .map(info -> BeanCopyUtils.copy(info, UserInfoVo::new))
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public UserInfoVo update(Long id) {
    dsl.update(QUserInfo.userInfo)
        .set(QUserInfo.userInfo.password, RandomStringUtils.random(6))
        .where(QUserInfo.userInfo.id.eq(id))
        .execute();
    UserInfo info =
        dsl.selectFrom(QUserInfo.userInfo).where(QUserInfo.userInfo.id.eq(id)).fetchOne();
    info = userinfo.findById(id).get();
    return BeanCopyUtils.copy(info, UserInfoVo::new);
  }
}
