package com.wjs.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wjs.takeout.dto.SetmealDto;
import com.wjs.takeout.entity.Setmeal;

/**
 * @author wjs
 * @createTime 2022-11-05 23:49
 */
public interface SetmealService extends IService<Setmeal> {
    //增加套餐
    void addSetmeal(SetmealDto setmealDto);
    //回显套餐
    SetmealDto queryAllDishMsgById(String id);
    //修改套餐
    void updateDishAndFlavors(SetmealDto setmealDto);
}
