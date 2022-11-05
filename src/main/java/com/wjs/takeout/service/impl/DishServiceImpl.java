package com.wjs.takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjs.takeout.entity.Dish;
import com.wjs.takeout.mapper.DishMapper;
import com.wjs.takeout.service.DishService;
import org.springframework.stereotype.Service;

/**
 * @author wjs
 * @createTime 2022-11-05 23:46
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
