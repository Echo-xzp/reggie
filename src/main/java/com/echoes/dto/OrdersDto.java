package com.echoes.dto;


import com.echoes.entity.OrderDetail;
import com.echoes.entity.Orders;
import lombok.Data;

import java.util.List;

@Data
public class OrdersDto extends Orders {

    private Integer sumNum;

    private List<OrderDetail> orderDetails;
	
}
