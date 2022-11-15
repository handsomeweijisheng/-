package com.wjs.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wjs.takeout.common.Result;
import com.wjs.takeout.dto.DishDto;
import com.wjs.takeout.dto.SetmealDto;
import com.wjs.takeout.entity.Category;
import com.wjs.takeout.entity.Dish;
import com.wjs.takeout.entity.Setmeal;
import com.wjs.takeout.entity.SetmealDish;
import com.wjs.takeout.service.CategoryService;
import com.wjs.takeout.service.SetmealDishService;
import com.wjs.takeout.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wjs
 * @createTime 2022-11-05 23:53
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;
    @PostMapping
    public Result<String> addSetmeal(@RequestBody SetmealDto setmealDto){
        setmealService.addSetmeal(setmealDto);
        return Result.success("增加套餐成功");
    }
//    分页查询
    @GetMapping("/page")
    public Result<Page<SetmealDto>> splitPages(int page,int pageSize,String name){
        //1.构造分页构造器
        Page<Setmeal> page1 = new Page<>(page, pageSize);
        //2.构造条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(name),Setmeal::getName,name);
        queryWrapper.eq(Setmeal::getIsDeleted,0);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        //3.执行查询
        setmealService.page(page1, queryWrapper);
        Page<SetmealDto> setmealDtoPage=new Page<>();
        BeanUtils.copyProperties(page1,setmealDtoPage,"records");
        List<Setmeal> records = page1.getRecords();
        List<SetmealDto> setmealDtos = records.stream().map(setmeal -> {
            SetmealDto setmealDto = new SetmealDto();
            Long categoryId = setmeal.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (!StringUtils.isEmpty(category)) {
                setmealDto.setCategoryName(category.getName());
            }
            BeanUtils.copyProperties(setmeal, setmealDto);
            return setmealDto;
        }).collect(Collectors.toList());
        setmealDtoPage.setRecords(setmealDtos);
        return Result.success(setmealDtoPage);
    }
    /**
     * 修改套餐,回显数据
     */
    @GetMapping("/{id}")
    public Result<SetmealDto> findAllDishMsgById(@PathVariable String id){
        //System.out.println(id);
        SetmealDto setmealDto=setmealService.queryAllDishMsgById(id);
        return Result.success(setmealDto);
    }
    /**
     * 修改套餐
     * @return 返回值
     */
    @PutMapping
    public Result<String> updateDish(@RequestBody SetmealDto setmealDto){
        //System.out.println("dishDto = " + dishDto);
        setmealService.updateDishAndFlavors(setmealDto);
        return Result.success("修改成功");
    }
    /**
     * 删除套餐
     */
    @DeleteMapping
    public Result<String> deleteDishes(String ids){
        String[] split = ids.split(",");
        for (String str :
                split) {
        //    1.修改套餐里面的状态
            Setmeal setmeal = new Setmeal();
            setmeal.setIsDeleted(1);
            LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Setmeal::getId,str);
            setmealService.update(setmeal,queryWrapper);
            //    2.修改套餐菜品表里面的数据
            SetmealDish setmealDish = new SetmealDish();
            setmealDish.setIsDeleted(1);
            LambdaQueryWrapper<SetmealDish> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(SetmealDish::getSetmealId,str);
            setmealDishService.update(setmealDish,queryWrapper1);
        }
        return Result.success("删除菜品成功");
    }
    /**
     * 套餐停售
     */
    @PostMapping("/status/0")
    public Result<String> statusOfDishRemove(String ids){
        //System.out.println(ids);
        String[] split = ids.split(",");
        for (String id :
                split) {
            LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Setmeal::getId,id);
            Setmeal setmeal = new Setmeal();
            setmeal.setStatus(0);
            setmealService.update(setmeal,queryWrapper);
        }
        return Result.success("停售成功");
    }
    /**
     * 套餐启售
     */
    @PostMapping("/status/1")
    public Result<String> statusOfDishSell(String ids){
        String[] split = ids.split(",");
        for (String id :
                split) {
            LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
            //queryWrapper.eq(Dish::getStatus,0);
            queryWrapper.eq(Setmeal::getId,id);
            Setmeal setmeal = new Setmeal();
            setmeal.setStatus(1);
            setmealService.update(setmeal,queryWrapper);
        }
        return Result.success("起售成功");
    }
}
