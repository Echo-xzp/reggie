package com.echoes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.echoes.dto.SetmealDto;
import com.echoes.entity.*;
import com.echoes.mapper.SetmealMapper;
import com.echoes.service.CategoryService;
import com.echoes.service.DishService;
import com.echoes.service.SetmealDishService;
import com.echoes.service.SetmealService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 套餐 服务实现类
 * </p>
 *
 * @author Xiao ZhiPeng
 * @since 2022-07-28
 */
@Service
@Transactional
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishService dishService;

    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);

        Long id = setmealDto.getId();
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(id);
        });
        setmealDishService.saveBatch(setmealDishes);

        return;
    }

    @Override
    public Page<SetmealDto> getSetmealPageWithCategoryName(int page, int pageSize, String name) {
        Page<Setmeal> setmealPage = new Page<>(page,pageSize);

        /*查询菜品分页*/
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Setmeal::getName, name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(setmealPage,queryWrapper);

        /*拷贝菜品属性给dishDto并添加菜品分类属性*/
        List<Setmeal> records = setmealPage.getRecords();
        List<SetmealDto> setmealDtos = new ArrayList<>();
        records.forEach(record ->{
            Long categoryId = record.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();

            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(record,setmealDto);
            setmealDto.setCategoryName(categoryName);
            setmealDtos.add(setmealDto);
        });

        Page<SetmealDto> setmealDtoPage = new Page<>();
        BeanUtils.copyProperties(setmealPage,setmealDtoPage);
        setmealDtoPage.setRecords(setmealDtos);

        return setmealDtoPage;
    }

    @Override
    public SetmealDto getByIdWithDish(Long id) {

        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> setmealDishes = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(setmealDishes);

        return setmealDto;
    }

    @Override
    public void updateWithDish(SetmealDto setmealDto) {
        /*更新套餐*/
        this.updateById(setmealDto);

        /*清空套餐菜品表*/
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());
        setmealDishService.remove(queryWrapper);

        /*保存新菜品*/
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.forEach(setmealDish ->{
            setmealDish.setSetmealId(setmealDto.getId());
        });
        setmealDishService.saveBatch(setmealDishes);

        return;
    }

    @Override
    public List<SetmealDto> listWithDish(LambdaQueryWrapper<Setmeal> queryWrapper) {

        List<Setmeal> setmeals = setmealService.list(queryWrapper);
        List<SetmealDto> setmealDtos = new ArrayList<>();
        setmeals.forEach(setmeal -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmeal, setmealDto);

            Long categoryId = setmeal.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }

            LambdaQueryWrapper<SetmealDish> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(SetmealDish::getSetmealId, setmeal.getId());
            List<SetmealDish> setmealDishes = setmealDishService.list(queryWrapper1);
            setmealDto.setSetmealDishes(setmealDishes);

            setmealDtos.add(setmealDto);
        });

        return setmealDtos;
    }
}
