package com.wjs.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjs.takeout.common.BaseContext;
import com.wjs.takeout.common.CustomException;
import com.wjs.takeout.entity.*;
import com.wjs.takeout.mapper.OrderMapper;
import com.wjs.takeout.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author wjs
 * @createTime 2022-11-16 21:38
 */
@Service
@Slf4j
@Transactional
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private UserService userService;
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private OrderDetailService orderDetailService;
    /**
     *
     * @param orders 用户下单
     */
    @Override
    public void sendOrder(Orders orders) {
    //    1.获取当前用户Id
        Long userId = BaseContext.getCurrentId();
        //    2.查询当前用户的购物车数据
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(queryWrapper);
        //计算金额价格
        AtomicInteger amount = new AtomicInteger(0);//此方法在高并发的情况下不会出现问题
        //        获取订单号
        long orderId = IdWorker.getId();
        //遍历出所有的订单以便于放到订单明细中
        List<OrderDetail> orderDetails = shoppingCartList.stream()//此过程中,需要遍历出所有购物车订单明细并计算价格
                .map(item -> {
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setOrderId(orderId);
                    orderDetail.setNumber(item.getNumber());
                    orderDetail.setDishFlavor(item.getDishFlavor());
                    orderDetail.setDishId(item.getDishId());
                    orderDetail.setSetmealId(item.getSetmealId());
                    orderDetail.setName(item.getName());
                    orderDetail.setImage(item.getImage());
                    orderDetail.setAmount(item.getAmount());
                    amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
                    return orderDetail;
                }).collect(Collectors.toList());
        if(StringUtils.isEmpty(shoppingCartList)){
            throw new CustomException("购物车无数据");
        }
        //2.1 查询用户数据
        User user = userService.getById(userId);
        //2.2 查询地址数据
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if(StringUtils.isEmpty(addressBook)){
            throw new CustomException("用户地址信息有误,无法下单");
        }
        //    3.向订单表插入一条数据


        //        保存订单表
        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));//总金额
        orders.setUserId(userId);
        orders.setNumber(String.valueOf(orderId));
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        this.save(orders);
    //    4.向订单明细表插入数据
        orderDetailService.saveBatch(orderDetails);
    //    5.清空购物车数据
        shoppingCartService.remove(queryWrapper);
    }
}
