package com.echoes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.echoes.common.R;
import com.echoes.dto.DishDto;
import com.echoes.entity.Category;
import com.echoes.entity.Dish;
import com.echoes.entity.DishFlavor;
import com.echoes.mapper.DishMapper;
import com.echoes.service.CategoryService;
import com.echoes.service.DishFlavorService;
import com.echoes.service.DishService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 菜品管理 服务实现类
 * </p>
 *
 * @author Xiao ZhiPeng
 * @since 2022-07-27
 */
@Service
@Transactional
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    /**
     * @Name : saveWithFlavor
     * @description : 新增菜品，并保存口味
     * @createTime : 2022/7/27 16:40
     * @param : dishDto
     * @return : void
     */
    @Override
    public void saveWithFlavor(DishDto dishDto) {

        this.save(dishDto); // dishDto隐式转换

        Long id = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.forEach(flavor ->{
            flavor.setDishId(id);
        });

        dishFlavorService.saveBatch(dishDto.getFlavors());

    }

    /**
     * @Name : getByIdWithFlavor
     * @description : 根据菜品id查询菜品信息与口味信息
     * @createTime : 2022/7/28 15:47
     * @param : id
     * @return : com.echoes.dto.DishDto
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> dishFlavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(dishFlavors);

        return dishDto;
    }

    @Override
    public void updateWithFlavor(DishDto dishDto) {

        /*更新菜品*/
        this.updateById(dishDto);

        /*清空口味表*/
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        /*保存新口味*/
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.forEach(flavor ->{
            flavor.setDishId(dishDto.getId());
        });
        dishFlavorService.saveBatch(flavors);

        return;
    }

    /**
     * @Name : getDishPageWithCategoryName
     * @description : 获取带分类名的菜品分页
     * @createTime : 2022/7/28 15:47
     * @param : page
     * @param : pageSize
     * @param : name
     * @return : com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.echoes.dto.DishDto>
     */
    @Override
    public Page<DishDto> getDishPageWithCategoryName(int page, int pageSize, String name) {
        Page<Dish> dishPage = new Page<>(page,pageSize);

        /*查询菜品分页*/
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Dish::getName, name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(dishPage,queryWrapper);

        /*拷贝菜品属性给dishDto并添加菜品分类属性*/
        List<Dish> records = dishPage.getRecords();
        List<DishDto> dishDtos = new ArrayList<>();
        records.forEach(record ->{
            Long categoryId = record.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();

            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(record,dishDto);
            dishDto.setCategoryName(categoryName);
            dishDtos.add(dishDto);
        });

        Page<DishDto> dishDtoPage = new Page<>();
        BeanUtils.copyProperties(dishPage,dishDtoPage);
        dishDtoPage.setRecords(dishDtos);

        return dishDtoPage;
    }

    @Override
    public List<DishDto> listWithFlavor(LambdaQueryWrapper<Dish> queryWrapper) {
        List<Dish> dishes = dishService.list(queryWrapper);
        List<DishDto> dishDtos = new ArrayList<>();
        dishes.forEach(dish -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish,dishDto);

            Long categoryId = dish.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            LambdaQueryWrapper<DishFlavor> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(DishFlavor::getDishId,dish.getId());
            List<DishFlavor> dishFlavors = dishFlavorService.list(queryWrapper1);
            dishDto.setFlavors(dishFlavors);

            dishDtos.add(dishDto);
        });

        return dishDtos;
    }
}
