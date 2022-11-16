package com.wjs.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wjs.takeout.common.BaseContext;
import com.wjs.takeout.common.Result;
import com.wjs.takeout.entity.ShoppingCart;
import com.wjs.takeout.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wjs
 * @createTime 2022-11-15 22:34
 */
@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @GetMapping("/list")
    public Result<List<ShoppingCart>> splitPage(){
        //System.out.println("wjsyyds");
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return Result.success(list);
    }
    @PostMapping("/add")
    public Result<ShoppingCart> addShopCart(@RequestBody ShoppingCart shoppingCart){
        //1.获取当前用户id
        Long id = BaseContext.getCurrentId();
        shoppingCart.setUserId(id);
        //2.获取菜品或则套餐Id
        Long dishId = shoppingCart.getDishId();
        if(dishId!=null){
        //    添加的是菜品,所以向数据库中添加菜品信息
        //    注意:添加之前先查询数据库中是否已经有该菜品,如果有的话就增加数量,否则就添加菜品
        //    查询的时候根据用户id和菜品id能唯一查出来一个
            LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ShoppingCart::getUserId,id);
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
            queryWrapper.eq(ShoppingCart::getDishFlavor,shoppingCart.getDishFlavor());
            ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);
            if(StringUtils.isEmpty(cartServiceOne)){
                shoppingCart.setCreateTime(LocalDateTime.now());
                shoppingCartService.save(shoppingCart);
            }else{
            //    增加数量
                Integer number = cartServiceOne.getNumber();
                int count=number+1;
                cartServiceOne.setNumber(count);
                shoppingCartService.updateById(cartServiceOne);
                shoppingCart=cartServiceOne;
            }
        }else {
        //    添加的是套餐,所以向数据库中添加套餐信息
            //    注意:添加之前先查询数据库中是否已经有该套餐,如果有的话就增加数量,否则就添加套餐
            //    查询的时候根据用户id和套餐id能唯一查出来一个
            LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ShoppingCart::getUserId,id);
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
            ShoppingCart one = shoppingCartService.getOne(queryWrapper);
            if(StringUtils.isEmpty(one)){
                shoppingCart.setCreateTime(LocalDateTime.now());
                shoppingCartService.save(shoppingCart);
            }else {
            //    增加数量
                Integer number = one.getNumber();
                int count=number+1;
                one.setNumber(count);
                shoppingCartService.updateById(one);
                shoppingCart=one;
            }
        }
        return Result.success(shoppingCart);
    }
    @PostMapping("/sub")
    public Result<String> subShoppingCar(@RequestBody ShoppingCart shoppingCart){
        if(shoppingCart.getDishId()!=null){
            LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
            //ShoppingCart serviceOne = shoppingCartService.getOne(queryWrapper);
            ShoppingCart serviceOne = shoppingCartService.getOne(queryWrapper);
            //从数据库中查询出来是否大于1如果只有一个直接删除
            //LambdaQueryWrapper<ShoppingCart> one = new LambdaQueryWrapper<>();
            //one.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
            //ShoppingCart serviceOne = shoppingCartService.getOne(one);
            if(serviceOne.getNumber()>1){
                Integer number = serviceOne.getNumber();
                number=number-1;
                shoppingCart.setNumber(number);
                shoppingCartService.update(shoppingCart,queryWrapper);
            }else {
                LambdaQueryWrapper<ShoppingCart> queryWrapper1 = new LambdaQueryWrapper<>();
                queryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
                //queryWrapper.eq();
                shoppingCartService.remove(queryWrapper);
            }
        }else {
            LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
            ShoppingCart serviceOne = shoppingCartService.getOne(queryWrapper);
            if(serviceOne.getNumber()>1){
                Integer number = serviceOne.getNumber();
                number=number-1;
                shoppingCart.setNumber(number);
                shoppingCartService.update(shoppingCart,queryWrapper);
            }else {
                LambdaQueryWrapper<ShoppingCart> queryWrapper1 = new LambdaQueryWrapper<>();
                queryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
                //queryWrapper.eq();
                shoppingCartService.remove(queryWrapper);
            }
            //shoppingCartService.remove(queryWrapper);
        }
        return Result.success("删除成功");
    }
    @DeleteMapping("/clean")
    public Result<String> deleteShoppingCar(){
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        shoppingCartService.remove(queryWrapper);
        return Result.success("清空购物车成功");
    }
}
