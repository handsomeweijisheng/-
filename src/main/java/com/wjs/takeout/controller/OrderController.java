package com.wjs.takeout.controller;

/**
 * @author wjs
 * @createTime 2022-11-16 21:39
 */

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wjs.takeout.common.BaseContext;
import com.wjs.takeout.common.Result;
import com.wjs.takeout.dto.OrdersDto;
import com.wjs.takeout.entity.OrderDetail;
import com.wjs.takeout.entity.Orders;
import com.wjs.takeout.service.OrderDetailService;
import com.wjs.takeout.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;
    @PostMapping("/submit")
    public Result<String> submitOrderMsg(@RequestBody Orders orders){
        orderService.sendOrder(orders);
        return Result.success("下单成功");
    }
    /**
     * 查询订单明细表
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("userPage")
    public Result<Page> orderDetail(Integer page, Integer pageSize) {
        Page<Orders> pageInfo = new Page<>(page, pageSize);

        Page<OrdersDto> ordersDtoPage = new Page<OrdersDto>();

        Long currentId = BaseContext.getCurrentId();
//        查询订单数据
        QueryWrapper<Orders> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", currentId);
        Page<Orders> ordersPage = orderService.page(pageInfo, wrapper);

        BeanUtils.copyProperties(pageInfo, ordersDtoPage, "records");

        List<Orders> ordersList = ordersPage.getRecords();

        List<OrdersDto> ordersDtoList = ordersList.stream().map(item -> {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item, ordersDto);

            Long orderId = item.getId();

            if (orderId != null) {
//                        查询订单明细表
                QueryWrapper<OrderDetail> 
                        queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("order_id", orderId);
                List<OrderDetail> orderDetailList = orderDetailService.list(queryWrapper);
                ordersDto.setOrderDetails(orderDetailList);
            }

            return ordersDto;

        }).collect(Collectors.toList());

        ordersDtoPage.setRecords(ordersDtoList);

        return Result.success(ordersDtoPage);

    }

}
