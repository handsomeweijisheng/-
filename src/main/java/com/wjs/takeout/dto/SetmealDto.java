package com.wjs.takeout.dto;

import com.wjs.takeout.entity.Setmeal;
import com.wjs.takeout.entity.SetmealDish;
import lombok.Data;

import java.util.List;

/**
 * @author wjs
 * @createTime 2022-11-14 16:18
 */
@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;//套餐关联的菜品集合

    private String categoryName;//分类名称
}