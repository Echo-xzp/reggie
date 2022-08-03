package com.echoes.service.impl;

import com.echoes.entity.OrderDetail;
import com.echoes.mapper.OrderDetailMapper;
import com.echoes.service.OrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单明细表 服务实现类
 * </p>
 *
 * @author Xiao ZhiPeng
 * @since 2022-07-30
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}
