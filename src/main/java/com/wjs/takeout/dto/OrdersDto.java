package com.wjs.takeout.dto;

import com.wjs.takeout.entity.OrderDetail;
import com.wjs.takeout.entity.Orders;
import lombok.Data;

import java.util.List;

/**
 * @author wjs
 * @createTime 2022-11-17 9:49
 */
@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;

}