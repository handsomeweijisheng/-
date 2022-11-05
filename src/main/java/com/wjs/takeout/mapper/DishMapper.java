package com.wjs.takeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wjs.takeout.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wjs
 * @createTime 2022-11-05 23:47
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
