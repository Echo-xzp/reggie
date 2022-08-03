package com.echoes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.echoes.common.CustomException;
import com.echoes.entity.Category;
import com.echoes.entity.Dish;
import com.echoes.entity.Setmeal;
import com.echoes.mapper.CategoryMapper;
import com.echoes.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.echoes.service.DishService;
import com.echoes.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜品及套餐分类 服务实现类
 * </p>
 *
 * @author Xiao ZhiPeng
 * @since 2022-07-27
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    /**
     * @Name : removeById
     * @description : 根据id,判断分类是否关联菜品来删除分类
     * @createTime : 2022/7/27 13:42
     * @param : id
     * @return : java.lang.Boolean
     */
    @Override
    public Boolean remove(Long id) {

        LambdaQueryWrapper<Dish> dishQueryWrapper = new LambdaQueryWrapper<>();
        dishQueryWrapper.eq(Dish::getCategoryId,id);
        int dishCount = dishService.count(dishQueryWrapper);
        if (dishCount > 0){
            throw new  CustomException("该分类关联了菜品，无法删除！");
        }

        LambdaQueryWrapper<Setmeal> setmealQueryWrapper = new LambdaQueryWrapper<>();
        setmealQueryWrapper.eq(Setmeal::getCategoryId,id);
        int setmealCount = setmealService.count(setmealQueryWrapper);
        if (setmealCount > 0){
            throw new  CustomException("该分类关联了套餐，无法删除！");
        }

        super.removeById(id);
        return true;
    }
}
