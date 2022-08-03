package com.echoes.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.echoes.dto.DishDto;
import com.echoes.dto.SetmealDto;
import com.echoes.entity.Dish;
import com.echoes.entity.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 套餐 服务类
 * </p>
 *
 * @author Xiao ZhiPeng
 * @since 2022-07-28
 */
public interface SetmealService extends IService<Setmeal> {
    public void saveWithDish(SetmealDto setmealDto);

    public Page<SetmealDto> getSetmealPageWithCategoryName(int page, int pageSize, String name);

    public SetmealDto getByIdWithDish(Long id);

    public void updateWithDish(SetmealDto setmealDto);

    public List<SetmealDto> listWithDish(LambdaQueryWrapper<Setmeal> queryWrapper);


}
