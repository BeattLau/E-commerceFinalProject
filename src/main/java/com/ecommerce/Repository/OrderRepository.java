package com.ecommerce.Repository;

import com.ecommerce.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findOrderByOrderId(Long orderId);
    Order findOrderByDate(Date date);

}