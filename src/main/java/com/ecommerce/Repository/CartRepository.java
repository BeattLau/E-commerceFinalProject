package com.ecommerce.Repository;

import com.ecommerce.Entity.CustomUser;
import com.ecommerce.Entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<ShoppingCart, Long> {
    ShoppingCart findShoppingCartByCartId(Long cartId);
    ShoppingCart findShoppingCartByUser(CustomUser customUser);
}