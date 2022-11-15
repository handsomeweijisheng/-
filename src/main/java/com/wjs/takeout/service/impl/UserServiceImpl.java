package com.wjs.takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjs.takeout.entity.User;
import com.wjs.takeout.mapper.UserMapper;
import com.wjs.takeout.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author wjs
 * @createTime 2022-11-15 19:27
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
