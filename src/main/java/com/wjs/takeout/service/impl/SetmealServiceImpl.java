package com.wjs.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjs.takeout.dto.SetmealDto;
import com.wjs.takeout.entity.Setmeal;
import com.wjs.takeout.entity.SetmealDish;
import com.wjs.takeout.mapper.SetmealMapper;
import com.wjs.takeout.service.SetmealDishService;
import com.wjs.takeout.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author wjs
 * @createTime 2022-11-05 23:50
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;
    //增加套餐
    @Override
    public void addSetmeal(SetmealDto setmealDto) {
        //向setmealDish表存放数据
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDto,setmeal);
        this.save(setmeal);
        setmealDishes.stream().forEach(setmealDish -> {
            Long id = setmeal.getId();
            setmealDish.setSetmealId(id);
            setmealDishService.save(setmealDish);
        });


    }
    //回显套餐
    @Override
    public SetmealDto queryAllDishMsgById(String id) {
        //1.从setmeal里面查询出套餐
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto=new SetmealDto();
        if(!StringUtils.isEmpty(setmeal)){
            BeanUtils.copyProperties(setmeal,setmealDto);
        }
        //2.从setmeal_dish里面查出来数据根据setmeal_id
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> list = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(list);
        return setmealDto;
    }
    //对套餐进行修改
    @Override
    public void updateDishAndFlavors(SetmealDto setmealDto) {
        List<SetmealDish> list = setmealDto.getSetmealDishes();
    //    1.先删除所有的setmeal_dish然后再插入
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());
        setmealDishService.remove(queryWrapper);
        list.stream()
                .forEach(setmealDish -> {
                    setmealDish.setSetmealId(setmealDto.getId());
                    setmealDishService.save(setmealDish);
                });
    //    2.修改setmeal
        this.updateById(setmealDto);
    }
}
