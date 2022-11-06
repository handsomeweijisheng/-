package com.wjs.takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjs.takeout.entity.DishFlavor;
import com.wjs.takeout.mapper.DishFlavorMapper;
import com.wjs.takeout.service.DishFlavorService;
import org.springframework.stereotype.Service;

/**
 * @author wjs
 * @createTime 2022-11-06 22:44
 */
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
