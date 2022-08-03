package com.echoes.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.echoes.common.BaseContext;
import com.echoes.common.R;
import com.echoes.dto.OrdersDto;
import com.echoes.entity.OrderDetail;
import com.echoes.entity.Orders;
import com.echoes.entity.ShoppingCart;
import com.echoes.service.OrderDetailService;
import com.echoes.service.OrdersService;
import com.echoes.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author Xiao ZhiPeng
 * @since 2022-07-30
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrdersController {
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private ShoppingCartService shoppingCartService;


    @PutMapping
    public R<String> update(@RequestBody Orders orders){
        log.info(orders.toString());

        ordersService.updateById(orders);
        return R.success("修改成功！");
    }


    /**
     * @Name : submit
     * @description : 用户下单
     * @createTime : 2022/7/31 15:38
     * @param : orders
     * @return : com.echoes.common.R<java.lang.String>
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){

        ordersService.submit(orders);

        return R.success("下单成功！");
    }

    /**
     * @Name : again
     * @description : 再来一单
     * @createTime : 2022/7/31 16:50
     * @param : orders
     * @return : com.echoes.common.R<com.echoes.entity.Orders>
     */
    @PostMapping("/again")
    public R<String> again(@RequestBody Orders orders){
        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(orders != null, OrderDetail::getOrderId,orders.getId());
        List<OrderDetail> orderDetailList = orderDetailService.list(queryWrapper);

        orderDetailList.forEach(orderDetail -> {
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart.setUserId(BaseContext.getCurrentId());
            BeanUtils.copyProperties(orderDetail,shoppingCart,"id");
            shoppingCartService.save(shoppingCart);
        });

        return R.success("添加订单成功!");
    }

    /**
     * @Name : page
     * @description : 管理系统查看总订单
     * @createTime : 2022/7/31 16:51
     * @param : page
     * @param : pageSize
     * @param : number
     * @param : beginTime
     * @param : endTime
     * @return : com.echoes.common.R<com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.echoes.entity.Orders>>
     */
    @GetMapping("/page")
    public R<Page<OrdersDto>> page(Integer page, Integer pageSize,
                                Long number, String beginTime, String endTime){

//        Page<Orders> ordersPage = new Page<>(page,pageSize);

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(number != null,Orders::getNumber,number);
        if (beginTime != null && endTime != null){

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime localBeginDateTime = LocalDateTime.parse(beginTime,formatter);
            LocalDateTime localEndDateTime = LocalDateTime.parse(endTime,formatter);
            queryWrapper.between(Orders::getOrderTime,localBeginDateTime,localEndDateTime);

        }
        queryWrapper.orderByDesc(Orders::getOrderTime);

//        ordersService.page(ordersPage,queryWrapper);
        Page<OrdersDto> ordersDtoPage = ordersService.getOrdersDtoPage(page, pageSize, queryWrapper);

        return R.success(ordersDtoPage);
    }

    /**
     * @Name : userPage
     * @description : 用户查看历史订单
     * @createTime : 2022/7/31 16:51
     * @param : page
     * @param : pageSize
     * @return : com.echoes.common.R<com.baomidou.mybatisplus.extension.plugins.pagination.Page>
     */
    @GetMapping("/userPage")
    public R<Page> userPage(Integer page, Integer pageSize){
        Long userId = BaseContext.getCurrentId();

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId,userId);
        queryWrapper.orderByDesc(Orders::getOrderTime);

        Page<OrdersDto> ordersDtoPage = ordersService.getOrdersDtoPage(page, pageSize, queryWrapper);

        return R.success(ordersDtoPage);
    }

}

