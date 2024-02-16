package com.ecommerce.Repository;

import com.ecommerce.Entity.CartItems;
import com.ecommerce.Entity.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CartItemsRepository extends JpaRepository<CartItems, Long> {
    Optional<CartItems> findById(Long cartItemsId);

    @Transactional
    @Modifying
    @Query("DELETE FROM CartItems c WHERE c.shoppingCart.cartId = :cartId AND c.shoppingCart.user = :currentUser AND c.purchased = true")
    void deletePurchasedItemsByCartId(@Param("cartId") Long cartId, @Param("currentUser") CustomUser currentUser);
}
