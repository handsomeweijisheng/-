package com.wjs.takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjs.takeout.entity.ShoppingCart;
import com.wjs.takeout.mapper.ShoppingCartMapper;
import com.wjs.takeout.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @author wjs
 * @createTime 2022-11-15 22:33
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
