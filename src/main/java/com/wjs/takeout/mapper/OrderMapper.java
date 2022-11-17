package com.wjs.takeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wjs.takeout.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wjs
 * @createTime 2022-11-16 21:36
 */
@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}