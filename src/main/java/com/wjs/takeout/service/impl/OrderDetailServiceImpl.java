package com.wjs.takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjs.takeout.entity.OrderDetail;
import com.wjs.takeout.mapper.OrderDetailMapper;
import com.wjs.takeout.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @author wjs
 * @createTime 2022-11-16 21:38
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
