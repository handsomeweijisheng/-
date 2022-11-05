package com.wjs.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjs.takeout.common.CustomException;
import com.wjs.takeout.entity.Category;
import com.wjs.takeout.entity.Dish;
import com.wjs.takeout.entity.Setmeal;
import com.wjs.takeout.mapper.CategoryMapper;
import com.wjs.takeout.service.CategoryService;
import com.wjs.takeout.service.DishService;
import com.wjs.takeout.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author wjs
 * @createTime 2022-11-05 22:25
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    //根据分类id移除菜品
    @Override
    public void remove(Long id) {
        //    先查出来dish表和Setmeal表是否有数据,如果有禁止移除
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId,id);
        if(dishService.count(queryWrapper)>0){
        //    抛一个业务异常,CustomException
            throw new CustomException("当前分类下关联了菜品信息");
        }
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Setmeal::getCategoryId,id);
        if(setmealService.count(wrapper)>0){
        //    抛一个业务异常,CustomException
            throw new CustomException("当前分类下关联了套餐信息");
        }
        //正常删除
        removeById(id);
        //return Result.success("success");
    }
}
