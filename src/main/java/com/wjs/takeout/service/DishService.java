package com.wjs.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wjs.takeout.dto.DishDto;
import com.wjs.takeout.entity.Dish;

/**
 * @author wjs
 * @createTime 2022-11-05 23:46
 */
public interface DishService extends IService<Dish> {
//    增加菜品
    public void addDishAndFlavors(DishDto dishDto);
}
