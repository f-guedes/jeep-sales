package com.promineotech.jeep.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import com.promineotech.jeep.entity.Order;
import com.promineotech.jeep.entity.OrderRequest;
import com.promineotech.jeep.service.JeepOrderService;

@RestController
public class DefaultJeepOrderController implements JeepOrderController {

  @Autowired
  private JeepOrderService jeepOrderService;
  
  @Override
  public Order createOrder(OrderRequest orderRequest) {
    return jeepOrderService.createOrder(orderRequest);
  }

}
