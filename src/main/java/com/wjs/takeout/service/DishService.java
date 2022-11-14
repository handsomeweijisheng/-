package com.wjs.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wjs.takeout.dto.DishDto;
import com.wjs.takeout.entity.Dish;

/**
 * @author wjs
 * @createTime 2022-11-05 23:46
 */
public interface DishService extends IService<Dish> {
     // 增加菜品
    public void addDishAndFlavors(DishDto dishDto);
    //根据id查询菜品并回显,为修改做准备
    DishDto queryAllDishMsgById(String id);
    //根据dishDto修改菜品
    void updateDishAndFlavors(DishDto dishDto);
}
