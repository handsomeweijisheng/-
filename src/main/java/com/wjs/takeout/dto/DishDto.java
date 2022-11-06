package com.wjs.takeout.dto;

import com.wjs.takeout.entity.Dish;
import com.wjs.takeout.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wjs
 * @createTime 2022-11-06 23:04
 */
@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}