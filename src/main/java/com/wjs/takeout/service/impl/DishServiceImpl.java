package com.wjs.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjs.takeout.dto.DishDto;
import com.wjs.takeout.entity.Dish;
import com.wjs.takeout.entity.DishFlavor;
import com.wjs.takeout.mapper.DishMapper;
import com.wjs.takeout.service.DishFlavorService;
import com.wjs.takeout.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wjs
 * @createTime 2022-11-05 23:46
 */
@Service
@Transactional
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    @Transactional
    @Override
    public void addDishAndFlavors(DishDto dishDto) {
        //保存到菜品表,因为dishDto是继承dish所以可以直接保存
        this.save(dishDto);
        //得到插入之后返回的dishId,并将其插入到dishFlavors表中
        Long id = dishDto.getId();
    //    保存到菜品口味表
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor dishFlavor :
                flavors) {
            dishFlavor.setDishId(id);
        }
        dishFlavorService.saveBatch(flavors);
    }

    /**
     *  根据id查询菜品信息回显
     * @param id id
     * @return 返回值
     */
    @Override
    public DishDto queryAllDishMsgById(String id) {
        DishDto dishDto=new DishDto();
        Dish dish = this.getById(id);
        BeanUtils.copyProperties(dish,dishDto);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(list);
        return dishDto;
    }

    /**
     *
     * 修改菜品
     */
    @Override
    public void updateDishAndFlavors(DishDto dishDto) {
    //    1.修改dish表
        this.updateById(dishDto);
    //    2.修改flavor表--->必须先删除后新增
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        dishDto.getFlavors().stream()
                .forEach(dishFlavor -> {
                    dishFlavor.setDishId(dishDto.getId());
                    dishFlavorService.save(dishFlavor);
                });
        //dishFlavorService.saveBatch(dishDto.getFlavors());
    }
}
