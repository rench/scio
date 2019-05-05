package com.scio.cloud.shardingjdbc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scio.cloud.shardingjdbc.domain.Order;
/**
 * @author Wang.ch
 * @date 2019-05-05 10:07:45
 */
@Repository
public interface OrderJpaRepository extends JpaRepository<Order, String> {

  Order findByOrderId(Long orderId);

  List<Order> findByUserId(String userId);
}
