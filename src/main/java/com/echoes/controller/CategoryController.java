package com.echoes.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.echoes.common.R;
import com.echoes.entity.Category;
import com.echoes.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 菜品及套餐分类 前端控制器
 * </p>
 *
 * @author Xiao ZhiPeng
 * @since 2022-07-27
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * @Name : save
     * @description : 新增菜品分类
     * @createTime : 2022/8/1 16:03
     * @param : category
     * @return : com.echoes.common.R<java.lang.String>
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
//        log.info("新增分类"+category.toString());

        if (categoryService.save(category)){
            return R.success("新增分类成功!");
        }
        return R.error("新增分类失败!");

    }

    /**
     * @Name : update
     * @description : 修改菜品分类
     * @createTime : 2022/8/1 16:03
     * @param : category
     * @return : com.echoes.common.R<java.lang.String>
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){

        categoryService.updateById(category);

        return R.success("修改信息成功！");
    }

    /**
     * @Name : delete
     * @description : 删除菜品分类
     * @createTime : 2022/8/1 16:03
     * @param : ids
     * @return : com.echoes.common.R<java.lang.String>
     */
    @DeleteMapping
    public R<String> delete(Long ids){

        log.info("删除分类"+ids);

        categoryService.remove(ids);

        return R.success("删除成功！");
    }

    /**
     * @Name : page
     * @description : 获取菜品分类分页数据
     * @createTime : 2022/8/1 16:04
     * @param : page
     * @param : pageSize
     * @return : com.echoes.common.R<com.baomidou.mybatisplus.extension.plugins.pagination.Page>
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize){

        Page<Category> pageInfo = new Page<>(page,pageSize);

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        categoryService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * @Name : list
     * @description : 根据id获取菜品分类列表
     * @createTime : 2022/8/1 16:04
     * @param : category
     * @return : com.echoes.common.R<java.util.List<com.echoes.entity.Category>>
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> categoryList = categoryService.list(queryWrapper);

        return R.success(categoryList);
    }

}

