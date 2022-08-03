package com.echoes.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.echoes.common.R;
import com.echoes.dto.SetmealDto;
import com.echoes.entity.Dish;
import com.echoes.entity.Setmeal;
import com.echoes.entity.SetmealDish;
import com.echoes.service.DishService;
import com.echoes.service.SetmealDishService;
import com.echoes.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 套餐 前端控制器
 * </p>
 *
 * @author Xiao ZhiPeng
 * @since 2022-07-28
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private DishService dishService;

    /**
     * @Name : save
     * @description : 新增套餐
     * @createTime : 2022/8/1 16:06
     * @param : setmealDto
     * @return : com.echoes.common.R<java.lang.String>
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info(setmealDto.toString());

        setmealService.saveWithDish(setmealDto);

        return R.success("新增成功！");
    }

    /**
     * @Name : update
     * @description : 更新菜品信息
     * @createTime : 2022/7/28 16:49
     * @param : dishDto
     * @return : com.echoes.common.R<java.lang.String>
     */
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){

        setmealService.updateWithDish(setmealDto);

        return R.success("修改成功！");
    }

    /**
     * @Name : delete
     * @description : 逻辑删除套餐
     * @createTime : 2022/7/28 16:50
     * @param : ids
     * @return : com.echoes.common.R<java.lang.String>
     */
    @DeleteMapping
    public R<String> delete(Long[] ids){
        for (Long id : ids) {
            if (setmealService.getById(id).getStatus() == 1){
                return R.error("非停售菜品不可删除！");
            }
            setmealService.removeById(id);
        }

        return R.success("删除成功！");
    }

    /**
     * @Name : getById
     * @description : 根据菜品id查询套餐信息与套餐中的菜品信息
     * @createTime : 2022/7/28 15:26
     * @param : id
     * @return : com.echoes.common.R<com.echoes.dto.DishDto>
     */
    @GetMapping("/{id}")
    public R<SetmealDto> getById(@PathVariable Long id){

        SetmealDto setmealDto = setmealService.getByIdWithDish(id);
        return R.success(setmealDto);
    }

    /**
     * @Name : page
     * @description : 获取套餐分页
     * @createTime : 2022/8/1 16:06
     * @param : page
     * @param : pageSize
     * @param : name
     * @return : com.echoes.common.R<com.baomidou.mybatisplus.extension.plugins.pagination.Page>
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        log.info(page+pageSize+name);

        Page<SetmealDto> setmealDtoPage = setmealService.getSetmealPageWithCategoryName(page, pageSize, name);

        return R.success(setmealDtoPage);

    }

    /**
     * @Name : list
     * @description : 获取套餐列表
     * @createTime : 2022/8/1 16:06
     * @param : setmeal
     * @return : com.echoes.common.R<java.util.List<com.echoes.entity.Setmeal>>
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null,
                Setmeal::getCategoryId,setmeal.getCategoryId());
        // 只查询起售的套餐
        queryWrapper.eq(Setmeal::getStatus,1);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(queryWrapper);

        return R.success(list);
    }

    /**
     * @Name : getDishesById
     * @description : 根据套餐id获取对应的菜品列表
     * @createTime : 2022/8/1 16:06
     * @param : id
     * @return : com.echoes.common.R<java.util.List<com.echoes.entity.Dish>>
     */
    @GetMapping("/dish/{id}")
    public R<List<Dish>> getDishesById(@PathVariable Long id){

        SetmealDto setmealDto = setmealService.getByIdWithDish(id);
        List<SetmealDish> dishes = setmealDto.getSetmealDishes();
        List<Dish> dishList = new ArrayList<>();
        dishes.forEach(setmealDish -> {
            Long dishId = setmealDish.getDishId();
            Dish dish = dishService.getById(dishId);
            dishList.add(dish);
        });
        return R.success(dishList);
    }

    /**
     * @Name : updateStatus
     * @description : 起售/停售套餐 修改套餐状态
     * @createTime : 2022/7/28 16:48
     * @param : status
     * @param : ids
     * @return : com.echoes.common.R<java.lang.String>
     */
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable Integer status, Long[] ids){
        log.info(ids.toString());

        for (Long id : ids ) {
            Setmeal setmeal = new Setmeal();
            setmeal.setStatus(status);
            setmeal.setId(id);
            if( !setmealService.updateById(setmeal) ){
                return null;
            }
        }

        return R.success("修改成功！");
    }

}

