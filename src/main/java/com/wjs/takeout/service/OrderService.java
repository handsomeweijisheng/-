package com.wjs.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wjs.takeout.entity.Orders;

/**
 * @author wjs
 * @createTime 2022-11-16 21:36
 */
public interface OrderService extends IService<Orders> {
    void sendOrder(Orders orders);
}