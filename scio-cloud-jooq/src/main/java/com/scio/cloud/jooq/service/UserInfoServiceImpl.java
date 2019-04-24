package com.scio.cloud.jooq.service;

import static com.scio.cloud.jooq.domain.public_.tables.UserInfo.USER_INFO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.jooq.DSLContext;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scio.cloud.jooq.domain.public_.tables.daos.UserInfoDao;
import com.scio.cloud.jooq.domain.public_.tables.pojos.UserInfo;
import com.scio.cloud.jooq.utils.BeanCopyUtils;
import com.scio.cloud.jooq.web.vo.UserInfoVo;
/**
 * user info service
 *
 * @author Wang.ch
 * @date 2019-04-15 10:29:47
 */
@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {
  final ModelMapper mapper = new ModelMapper();
  @Autowired private UserInfoDao userinfo;
  @Autowired private DSLContext dsl;

  @Override
  @Transactional
  public UserInfoVo save(UserInfoVo vo) {
    UserInfo save = BeanCopyUtils.copy(vo, UserInfo::new);
    userinfo.insert(save);
    return BeanCopyUtils.copy(save, UserInfoVo::new);
  }

  @Override
  @Transactional(readOnly = true)
  public UserInfoVo findById(Long id) {
    return Optional.ofNullable(userinfo.fetchOneById(id))
        .map(info -> BeanCopyUtils.copy(info, UserInfoVo::new))
        .orElse(null);
  }

  @Override
  @Transactional(readOnly = true)
  public UserInfoVo findByUsername(String username) {
    return Optional.ofNullable(userinfo.fetchOneByUsername(username))
        .map(info -> BeanCopyUtils.copy(info, UserInfoVo::new))
        .orElse(null);
  }

  @Override
  @Transactional
  public List<UserInfoVo> save(List<UserInfoVo> userinfos) {
    List<UserInfo> list =
        userinfos
            .stream()
            .map(info -> BeanCopyUtils.copy(info, UserInfo::new))
            .collect(Collectors.toList());
    userinfo.insert(list);
    return list.stream()
        .map(info -> BeanCopyUtils.copy(info, UserInfoVo::new))
        .collect(Collectors.toList());
  }

  @Override
  public List<UserInfoVo> findAll() {
    List<UserInfo> list = dsl.selectFrom(USER_INFO).fetch().into(UserInfo.class);
    return list.stream()
        .map(info -> BeanCopyUtils.copy(info, UserInfoVo::new))
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public UserInfoVo update(Long id) {
    dsl.update(USER_INFO)
        .set(USER_INFO.PASSWORD, RandomStringUtils.random(6))
        .where(USER_INFO.ID.eq(id))
        .execute();
    UserInfo info =
        dsl.selectFrom(USER_INFO).where(USER_INFO.ID.eq(id)).fetchOne().into(UserInfo.class);
    return BeanCopyUtils.copy(info, UserInfoVo::new);
  }
}
