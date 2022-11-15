package com.wjs.takeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wjs.takeout.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wjs
 * @createTime 2022-11-15 19:26
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
