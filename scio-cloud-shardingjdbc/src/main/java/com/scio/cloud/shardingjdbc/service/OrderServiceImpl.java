package com.scio.cloud.shardingjdbc.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scio.cloud.shardingjdbc.domain.Order;
import com.scio.cloud.shardingjdbc.repository.OrderJpaRepository;
import com.scio.cloud.shardingjdbc.util.Sequence;
/**
 * @author Wang.ch
 * @date 2019-05-05 10:11:51
 */
@Service
public class OrderServiceImpl implements OrderService {
  @Autowired private OrderJpaRepository jpa;

  @Override
  public Order createOrder(Order order) {
    order.setId(Sequence.get().nextId());
    order.setOrderId(Sequence.get().nextId());
    order.setModifiedDate(new Date());
    order.setCreatedDate(new Date());
    return jpa.save(order);
  }

  @Override
  public Order findByOrderId(Long orderId) {
    return jpa.findByOrderId(orderId);
  }

  @Override
  public List<Order> findByUserId(String userId) {
    return jpa.findByUserId(userId);
  }
}
