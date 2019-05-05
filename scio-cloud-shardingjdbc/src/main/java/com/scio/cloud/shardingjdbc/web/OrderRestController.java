package com.scio.cloud.shardingjdbc.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scio.cloud.shardingjdbc.domain.Order;
import com.scio.cloud.shardingjdbc.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderRestController {

  @Autowired private OrderService service;

  @RequestMapping("/create")
  public Order createOrder(Order order) {
    return service.createOrder(order);
  }

  @RequestMapping("/findByOrderId")
  public Order findById(Long orderId) {
    return service.findByOrderId(orderId);
  }

  @RequestMapping("/findByUserId")
  public List<Order> findByUserId(String userId) {
    return service.findByUserId(userId);
  }
}
