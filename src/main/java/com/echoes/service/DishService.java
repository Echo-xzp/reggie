package com.echoes.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.echoes.common.R;
import com.echoes.dto.DishDto;
import com.echoes.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜品管理 服务类
 * </p>
 *
 * @author Xiao ZhiPeng
 * @since 2022-07-27
 */
public interface DishService extends IService<Dish> {

    public void saveWithFlavor(DishDto dishDto);

    public DishDto getByIdWithFlavor(Long id);

    public void updateWithFlavor(DishDto dishDto);

    public Page<DishDto> getDishPageWithCategoryName(int page, int pageSize, String name);

    public List<DishDto> listWithFlavor(LambdaQueryWrapper<Dish> queryWrapper);

}
