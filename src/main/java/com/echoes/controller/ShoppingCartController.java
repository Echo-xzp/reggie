package com.echoes.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.echoes.common.BaseContext;
import com.echoes.common.R;
import com.echoes.entity.ShoppingCart;
import com.echoes.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 购物车 前端控制器
 * </p>
 *
 * @author Xiao ZhiPeng
 * @since 2022-07-30
 */
@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * @Name : add
     * @description : 购物车添加菜品或套餐
     * @createTime : 2022/7/31 15:39
     * @param : shoppingCart
     * @return : com.echoes.common.R<com.echoes.entity.ShoppingCart>
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){

        log.info("购物车数据 ：{}",shoppingCart);

        // 设置购物车用户所属
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        // 区分套餐和菜品，分类查询
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        Long dishId = shoppingCart.getDishId();
        if (null != dishId){
            // 菜品
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else {
            // 套餐
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }

        // 检查现有购物车是否已存在该套餐|菜品
        ShoppingCart cart = shoppingCartService.getOne(queryWrapper);
        if (cart != null) {
            // 存在，数目加1
            Integer number = cart.getNumber();
            cart.setNumber(number+1);
            shoppingCartService.updateById(cart);

        }else {
            // 不存在，存入新数据
            shoppingCart.setNumber(1);  // 设置默认数目 1
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            log.info(shoppingCart.toString());
            cart = shoppingCart;
        }

        return R.success(cart);
    }

    /**
     * @Name : add
     * @description : 购物车减少菜品或套餐
     * @createTime : 2022/7/31 15:39
     * @param : shoppingCart
     * @return : com.echoes.common.R<com.echoes.entity.ShoppingCart>
     */
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){

        log.info("购物车数据 ：{}",shoppingCart);

        // 设置购物车用户所属
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        // 区分套餐和菜品，分类查询
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        Long dishId = shoppingCart.getDishId();
        if (null != dishId){
            // 菜品
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else {
            // 套餐
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }

        // 检查现有购物车是否已存在该套餐|菜品
        ShoppingCart cart = shoppingCartService.getOne(queryWrapper);
        if (cart != null) {
            // 存在，数目减1
            Integer number = cart.getNumber();
            cart.setNumber(number-1);
            shoppingCartService.updateById(cart);
            // 数目为0，将购物车数据从数据库中清除
            if (cart.getNumber() <= 0){
                shoppingCartService.removeById(cart.getId());
            }

            return R.success(cart);

        }else {
            // 不存在，不做处理
            return R.success(shoppingCart);
        }

    }

    /**
     * @Name : list
     * @description : 获取当前用户购物车信息
     * @createTime : 2022/7/31 15:39
     * @Param : void
     * @return : com.echoes.common.R<java.util.List<com.echoes.entity.ShoppingCart>>
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> shoppingCartList = shoppingCartService.list(queryWrapper);

        return R.success(shoppingCartList);
    }

    /**
     * @Name : clean
     * @description : 清空购物车
     * @createTime : 2022/7/31 15:40
     * @Param : null
     * @return : com.echoes.common.R<java.lang.String>
     */
    @DeleteMapping("/clean")
    public R<String> clean(){
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        shoppingCartService.remove(queryWrapper);

        return R.success("清空成功!");
    }

}

