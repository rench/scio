package com.scio.cloud.beetlsql.mapper;

import java.util.List;

import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.annotatoin.SqlResource;
import org.beetl.sql.core.mapper.BaseMapper;

import com.scio.cloud.beetlsql.entity.UserInfo;

/**
 * Mapper 接口
 *
 * @author Wang.ch
 * @since 2019-04-25
 */
@SqlResource("userinfo")
public interface UserInfoMapper extends BaseMapper<UserInfo> {
  // sqlManager.select("userinfo.selectByUsername")
  UserInfo selectByUsername(@Param("username") String username);
  // for java 8+ ,javac with -parameters,can without @Param
  List<UserInfo> selectByUsernameLike(@Param("username") String username);
}
