package com.wjs.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wjs.takeout.entity.Category;

/**
 * @author wjs
 * @createTime 2022-11-05 22:24
 */
public interface CategoryService extends IService<Category> {
    //根据分类id移除菜品
      void remove(Long id);
}
