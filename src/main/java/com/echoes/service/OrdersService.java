package com.echoes.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.echoes.dto.OrdersDto;
import com.echoes.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author Xiao ZhiPeng
 * @since 2022-07-30
 */
public interface OrdersService extends IService<Orders> {
    public void submit(Orders orders);

    public Page<OrdersDto> getOrdersDtoPage( Integer page, Integer pageSize, LambdaQueryWrapper<Orders> queryWrapper);


    }
