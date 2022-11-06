package com.wjs.takeout.controller;

import com.wjs.takeout.common.Result;
import com.wjs.takeout.dto.DishDto;
import com.wjs.takeout.entity.Dish;
import com.wjs.takeout.entity.DishFlavor;
import com.wjs.takeout.service.DishFlavorService;
import com.wjs.takeout.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wjs
 * @createTime 2022-11-06 21:03
 */
@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @PostMapping
    public Result<String> addDish(@RequestBody DishDto dishDto){
        System.out.println(dishDto);
        //dishService.save(dish);
        //System.out.println(dishFlavor);flavors
        return Result.success("增加菜品成功");
    }
}
