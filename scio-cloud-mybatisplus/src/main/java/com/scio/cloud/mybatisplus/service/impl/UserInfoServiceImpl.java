package com.scio.cloud.mybatisplus.service.impl;

import com.scio.cloud.mybatisplus.entity.UserInfo;
import com.scio.cloud.mybatisplus.mapper.UserInfoMapper;
import com.scio.cloud.mybatisplus.service.IUserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Wang.ch
 * @since 2019-04-25
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

}
