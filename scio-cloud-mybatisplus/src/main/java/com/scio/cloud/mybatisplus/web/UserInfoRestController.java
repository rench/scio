package com.scio.cloud.mybatisplus.web;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scio.cloud.mybatisplus.config.UserStatus;
import com.scio.cloud.mybatisplus.entity.UserInfo;
import com.scio.cloud.mybatisplus.service.IUserInfoService;
import com.scio.cloud.mybatisplus.utils.BeanCopyUtils;
import com.scio.cloud.mybatisplus.web.vo.UserInfoVo;

/**
 * user info controller
 *
 * @author Wang.ch
 * @date 2019-03-25 09:24:37
 */
@RestController
public class UserInfoRestController {
  @Autowired private IUserInfoService userinfo;
  /**
   * find user info by id
   *
   * @param id
   * @return
   */
  @RequestMapping("/{id}")
  public UserInfoVo findById(@PathVariable(name = "id") Long id) {
    return BeanCopyUtils.copy(userinfo.getById(id), UserInfoVo::new);
  }
  /**
   * save user info
   *
   * @return
   */
  @RequestMapping("/save")
  public UserInfoVo save() {
    UserInfoVo vo = new UserInfoVo();
    vo.setUsername(RandomStringUtils.randomAlphabetic(12));
    vo.setPassword(RandomStringUtils.randomAlphabetic(12));
    vo.setRealname(RandomStringUtils.random(5, true, false));
    vo.setStatus(UserStatus.NORMAL.getValue());
    UserInfo info = BeanCopyUtils.copy(vo, UserInfo::new);
    userinfo.save(info);
    vo.setId(info.getId());
    return vo;
  }

  /**
   * save user info
   *
   * @return
   */
  @RequestMapping("/batchSave")
  public List<UserInfoVo> batchSave() {
    List<UserInfo> userinfos =
        Stream.of(1, 2, 3)
            .map(
                i -> {
                  UserInfo vo = new UserInfo();
                  vo.setUsername(RandomStringUtils.randomAlphabetic(12));
                  vo.setPassword(RandomStringUtils.randomAlphabetic(12));
                  vo.setRealname(RandomStringUtils.random(5, true, false));
                  vo.setStatus(UserStatus.NORMAL.getValue());
                  return vo;
                })
            .collect(Collectors.toList());
    userinfo.saveBatch(userinfos);

    return userinfos
        .stream()
        .map(u -> BeanCopyUtils.copy(u, UserInfoVo::new))
        .collect(Collectors.toList());
  }
  /**
   * find user info
   *
   * @param username
   * @return
   */
  @RequestMapping("/find")
  public UserInfoVo findByUsername(String username) {
    UserInfo info = new UserInfo();
    info.setUsername(username);
    info = userinfo.getOne(new QueryWrapper<UserInfo>(info));
    if (info == null) {
      return null;
    }
    return BeanCopyUtils.copy(info, UserInfoVo::new);
  }

  /**
   * find user info
   *
   * @param username
   * @return
   */
  @RequestMapping("/findLike")
  public List<UserInfoVo> findByUsernameLike(String username) {
    List<UserInfo> infos = userinfo.list(new QueryWrapper<UserInfo>().like("USERNAME", username));
    if (infos == null) {
      return null;
    }
    return infos
        .stream()
        .map(info -> BeanCopyUtils.copy(info, UserInfoVo::new))
        .collect(Collectors.toList());
  }

  /**
   * find all user info use querydsl
   *
   * @return
   */
  @RequestMapping("/findAll")
  public List<UserInfoVo> findAll() {
    return userinfo
        .list()
        .stream()
        .map(u -> BeanCopyUtils.copy(u, UserInfoVo::new))
        .collect(Collectors.toList());
  }

  @RequestMapping("/update/{id}")
  public UserInfoVo update(@PathVariable Long id) {
    UserInfo info = new UserInfo();
    info.setId(id);
    info.setPassword(RandomStringUtils.randomAlphabetic(8));
    userinfo.updateById(info);
    return BeanCopyUtils.copy(userinfo.getById(id), UserInfoVo::new);
  }
}
