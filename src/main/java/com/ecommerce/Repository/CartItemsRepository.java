package com.ecommerce.Repository;

import com.ecommerce.Entity.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemsRepository extends JpaRepository<CartItems, Long> {

}
