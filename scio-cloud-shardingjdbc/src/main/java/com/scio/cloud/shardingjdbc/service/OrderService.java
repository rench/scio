package com.scio.cloud.shardingjdbc.service;

import java.util.List;

import com.scio.cloud.shardingjdbc.domain.Order;

public interface OrderService {

  Order createOrder(Order order);

  Order findByOrderId(Long orderId);

  List<Order> findByUserId(String userId);
}
