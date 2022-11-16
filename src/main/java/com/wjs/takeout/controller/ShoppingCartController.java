package com.wjs.takeout.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wjs.takeout.common.Result;
import com.wjs.takeout.entity.ShoppingCart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wjs
 * @createTime 2022-11-15 22:34
 */
@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @GetMapping("/list")
    public Page<Result<ShoppingCart>> splitPage(){
        return null;
    }
}
