package com.ecommerce.Repository;

import com.ecommerce.Entity.CustomUser;
import com.ecommerce.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findOrderByUser_UserId(Long userId);
    Order findOrderByOrderId(Long orderId);
    Order findOrderByDate(Date date);
    List<Order> findOrdersByUserOrderByDateDesc(CustomUser user);
}
