package com.wjs.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wjs.takeout.common.Result;
import com.wjs.takeout.entity.Category;
import com.wjs.takeout.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wjs
 * @createTime 2022-11-05 22:27
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     *
     * @param category 分类
     * @return 返回值
     */
    @PostMapping
    public Result<String> addCategory(@RequestBody Category category) {
        //log.info("{}",category);
        categoryService.save(category);
        return Result.success("successful");
    }

    /**
     *
     * @param page 当前页
     * @param pageSize 每页条数
     * @return 返回值
     */
    @GetMapping("/page")
    public Result<Page<Category>> splitPage(int page, int pageSize) {
        //1.构造分页构造器
        Page<Category> page1 = new Page<>(page, pageSize);
        //2.构造条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        queryWrapper.orderByDesc(Category::getUpdateTime);
        //执行查询
        categoryService.page(page1, queryWrapper);
        return Result.success(page1);
    }

    /**
     *
     * @param category 修改分类
     * @return 返回值
     */
    @PutMapping
    public Result<String> updateCategory(@RequestBody Category category) {
        categoryService.updateById(category);
        return Result.success("修改成功");
    }

    /**
     *
     * @param ids 删除id
     * @return 返回值
     */
    @DeleteMapping
    public Result<String> deleteCategory(Long ids) {
        //categoryService.removeById(ids);
         categoryService.remove(ids);
        return Result.success("删除成功");
    }

    /**
     *
     * @return 在添加菜品的时候动态获取菜品分类下拉框东西
     */
    @GetMapping("/list")
    public Result<List<Category>> getCategoryList(Category category){
        List<Category> list=null;
        if(category .getType()==null){
            list = categoryService.list();
        }else{
            LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Category::getType,category.getType());
            queryWrapper.orderByAsc(Category::getSort);
            queryWrapper.orderByDesc(Category::getUpdateTime);
            list = categoryService.list(queryWrapper);
        }

        return Result.success(list);
    }
}
