package com.scio.cloud.shardingjdbc.web;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scio.cloud.shardingjdbc.service.UserInfoService;
import com.scio.cloud.shardingjdbc.util.GlobalIds;
import com.scio.cloud.shardingjdbc.web.vo.UserInfoVo;

/**
 * user info controller
 *
 * @author Wang.ch
 * @date 2019-03-25 09:24:37
 */
@RestController
@RequestMapping("/user")
public class UserInfoRestController {
  @Autowired private UserInfoService userinfo;
  /**
   * find user info by id
   *
   * @param id
   * @return
   */
  @RequestMapping("/{id}")
  public UserInfoVo findById(@PathVariable(name = "id") String id) {
    return userinfo.findById(id);
  }
  /**
   * save user info
   *
   * @return
   */
  @RequestMapping("/save")
  public UserInfoVo save() {
    UserInfoVo vo = new UserInfoVo();
    vo.setName(RandomStringUtils.randomAlphabetic(10));
    vo.setAddress(RandomStringUtils.randomAlphanumeric(10));
    vo.setLocked(false);
    vo.setMobile(RandomStringUtils.randomNumeric(11));
    vo.setId(GlobalIds.nextId().toString());
    vo = userinfo.save(vo);
    return vo;
  }

  /**
   * save user info
   *
   * @return
   */
  @RequestMapping("/batchSave")
  public List<UserInfoVo> batchSave() {
    List<UserInfoVo> userinfos =
        Stream.of(1, 2, 3)
            .map(
                i -> {
                  UserInfoVo vo = new UserInfoVo();
                  vo.setName(RandomStringUtils.randomAlphabetic(10));
                  vo.setAddress(RandomStringUtils.randomAlphanumeric(10));
                  vo.setLocked(false);
                  vo.setMobile(RandomStringUtils.randomNumeric(11));
                  vo.setId(GlobalIds.nextId().toString());
                  return vo;
                })
            .collect(Collectors.toList());
    return userinfo.save(userinfos);
  }
  /**
   * find user info
   *
   * @param username
   * @return
   */
  @RequestMapping("/find")
  public UserInfoVo findByUsername(String name) {
    UserInfoVo vo = userinfo.findByName(name);
    return vo;
  }

  @RequestMapping("/findAll")
  public List<UserInfoVo> findAll() {
    return userinfo.findAll();
  }
}
