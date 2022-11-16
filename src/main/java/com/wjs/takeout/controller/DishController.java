package com.wjs.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wjs.takeout.common.Result;
import com.wjs.takeout.dto.DishDto;
import com.wjs.takeout.entity.Category;
import com.wjs.takeout.entity.Dish;
import com.wjs.takeout.entity.DishFlavor;
import com.wjs.takeout.service.CategoryService;
import com.wjs.takeout.service.DishFlavorService;
import com.wjs.takeout.service.DishService;
import io.netty.util.internal.StringUtil;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private CategoryService categoryService;

    /**
     *
     * @param dishDto 利用dishDto增加菜品和菜品口味表
     * @return 返回值
     */
    @PostMapping
    public Result<String> addDish(@RequestBody DishDto dishDto){
        //dishService.save(dish);
        dishService.addDishAndFlavors(dishDto);
        return Result.success("增加菜品成功");
    }
    /**
     * 菜品分页功能
     */
    @GetMapping("/page")
    public Result<Page<DishDto>> splitPages(Long page, Long pageSize,String name){
        //1.构造分页构造器
        Page<Dish> dishPage = new Page<Dish>(page, pageSize);
        Page<DishDto> dishDtoPage=new Page<>();
        //2.构造条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(name),Dish::getName,name);
        queryWrapper.orderByAsc(Dish::getSort);
        queryWrapper.eq(Dish::getIsDeleted,0);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(dishPage,queryWrapper);
        //对象拷贝------->new 知识
        BeanUtils.copyProperties(dishPage,dishDtoPage,"records");
        List<Dish> records = dishPage.getRecords();
        //函数式编程遍历出所有的dish赋值给dishDto主要让其中的categoryName不是空
        List<DishDto> list= records.stream().map((item)->{
            DishDto dishDto=new DishDto();
            //拷贝对象
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();
        //    根据id查询分类对象
            Category category = categoryService.getById(categoryId);
            if(category!=null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);
        return Result.success(dishDtoPage);
    }
    /**
     * 菜品停售
     */
    @PostMapping("/status/0")
    public Result<String> statusOfDishRemove(String ids){
        //System.out.println(ids);
        String[] split = ids.split(",");
        for (String id :
                split) {
            LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Dish::getId,id);
            Dish dish = new Dish();
            dish.setStatus(0);
            dishService.update(dish,queryWrapper);
        }
        return Result.success("停售成功");
    }
    /**
     * 菜品启售
     */
    @PostMapping("/status/1")
    public Result<String> statusOfDishSell(String ids){
        String[] split = ids.split(",");
        for (String id :
                split) {
            LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
            //queryWrapper.eq(Dish::getStatus,0);
            queryWrapper.eq(Dish::getId,id);
            Dish dish = new Dish();
            dish.setStatus(1);
            dishService.update(dish,queryWrapper);
        }
        return Result.success("起售成功");
    }
    /**
     * 删除菜品
     */
    @DeleteMapping
    public Result<String> deleteDishes(String ids){
        String[] split = ids.split(",");
        for (String str :
                split) {
            //System.out.println(str);
            LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Dish::getId,str);
            Dish one = dishService.getOne(wrapper);
            if(one.getStatus()==0){
                return Result.error("停售商品不允许删除");
            }
            //通过ids修改菜品口味表,然后修改菜品表
            LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
            DishFlavor dishFlavor = new DishFlavor();
            dishFlavor.setIsDeleted(1);
            queryWrapper.eq(DishFlavor::getDishId,str);
            //queryWrapper.eq(DishFlavor::getIsDeleted,1);
            dishFlavorService.update(dishFlavor,queryWrapper);
            LambdaQueryWrapper<Dish> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(Dish::getId,str);
            Dish dish = new Dish();
            dish.setIsDeleted(1);
            //queryWrapper1.eq(Dish::getIsDeleted,1);
            //dishService.removeById(str);
            dishService.update(dish,queryWrapper1);
        }
        return Result.success("删除菜品成功");
    }
    /**
     * 修改菜品,回显数据
     */
    @GetMapping("/{id}")
    public Result<DishDto> findAllDishMsgById(@PathVariable String id){
        //System.out.println(id);
        DishDto dishDto=dishService.queryAllDishMsgById(id);
        return Result.success(dishDto);
    }

    /**
     * 修改菜品
     * @return 返回值
     */
    @PutMapping
    public Result<String> updateDish(@RequestBody DishDto dishDto){
        //System.out.println("dishDto = " + dishDto);
        dishService.updateDishAndFlavors(dishDto);
     return Result.success("修改成功");
    }
    /**
     * 套餐里面的添加菜品
     */
    @GetMapping("/list")
    public Result<List<DishDto>> getList(Dish dish){
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        queryWrapper.eq(Dish::getStatus,1);
        List<Dish> dishList = dishService.list(queryWrapper);
        List<DishDto> dishDtos = dishList.stream().map(dish1 -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish1, dishDto);
            Long dishId = dish1.getId();
            LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DishFlavor::getDishId, dishId);
            List<DishFlavor> dishFlavorList = dishFlavorService.list(wrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());
        return Result.success(dishDtos);
    }
}
