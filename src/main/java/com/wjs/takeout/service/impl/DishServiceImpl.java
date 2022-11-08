package com.wjs.takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjs.takeout.dto.DishDto;
import com.wjs.takeout.entity.Dish;
import com.wjs.takeout.entity.DishFlavor;
import com.wjs.takeout.mapper.DishMapper;
import com.wjs.takeout.service.DishFlavorService;
import com.wjs.takeout.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wjs
 * @createTime 2022-11-05 23:46
 */
@Service
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
}
