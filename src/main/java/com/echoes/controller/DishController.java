package com.echoes.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.echoes.common.R;
import com.echoes.dto.DishDto;
import com.echoes.entity.Category;
import com.echoes.entity.Dish;
import com.echoes.entity.Setmeal;
import com.echoes.entity.SetmealDish;
import com.echoes.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 菜品表 前端控制器
 * </p>
 *
 * @author Xiao ZhiPeng
 * @since 2022-07-27
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * @Name : save
     * @description : 新增菜品信息
     * @createTime : 2022/7/27 17:06
     * @param : dishDto
     * @return : com.echoes.common.R<java.lang.String>
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.saveWithFlavor(dishDto);

        return R.success("新增菜品成功！");
    }

    /**
     * @Name : update
     * @description : 更新菜品信息
     * @createTime : 2022/7/28 16:49
     * @param : dishDto
     * @return : com.echoes.common.R<java.lang.String>
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){

        dishService.updateWithFlavor(dishDto);

        return R.success("修改成功！");
    }

    /**
     * @Name : delete
     * @description : 逻辑删除菜品
     * @createTime : 2022/7/28 16:50
     * @param : ids
     * @return : com.echoes.common.R<java.lang.String>
     */
    @DeleteMapping
    public R<String> delete(Long[] ids){
        for (Long id : ids) {

            LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SetmealDish::getDishId,id);
            List<SetmealDish> list = setmealDishService.list(queryWrapper);

            if (list != null){
                return R.error("该菜品被其他套餐包含!");

            }else if (dishService.getById(id).getStatus() == 1){
                return R.error("非停售菜品不可删除！");
            }
            dishService.removeById(id);
        }

        return R.success("删除成功！");
    }

    /**
     * @Name : getById
     * @description : 根据菜品id查询菜品信息与口味信息
     * @createTime : 2022/7/28 15:26
     * @param : id
     * @return : com.echoes.common.R<com.echoes.dto.DishDto>
     */
    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id){

        DishDto dishDto = dishService.getByIdWithFlavor(id);

        return R.success(dishDto);
    }


    /**
     * @Name : page
     * @description : 查询菜品信息
     * @createTime : 2022/7/28 15:10
     * @param : page
     * @param : pageSize
     * @param : name
     * @return : com.echoes.common.R<com.baomidou.mybatisplus.extension.plugins.pagination.Page>
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){

        Page<DishDto> dishDtoPage = dishService.getDishPageWithCategoryName(page, pageSize, name);

        return R.success(dishDtoPage);
    }

    /**
     * @Name : list
     * @description : 根据分类名查询菜品集合
     * @createTime : 2022/7/28 17:12
     * @param : dish
     * @return : com.echoes.common.R<java.util.List<com.echoes.entity.Dish>>
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null,
                        Dish::getCategoryId,dish.getCategoryId());
        // 只查询起售的菜品
        queryWrapper.eq(Dish::getStatus,1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<DishDto> list = dishService.listWithFlavor(queryWrapper);

        return R.success(list);
    }


    /**
     * @Name : updateStatus
     * @description : 起售/停售菜品 修改菜品状态
     * @createTime : 2022/7/28 16:48
     * @param : status
     * @param : ids
     * @return : com.echoes.common.R<java.lang.String>
     */
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable Integer status, Long[] ids){
        log.info(ids.toString());

        for (Long id : ids ) {
            Dish dish = new Dish();
            dish.setStatus(status);
            dish.setId(id);
            if( !dishService.updateById(dish) ){
                return null;
            }
        }

        return R.success("修改成功！");
    }


}

