package com.ecommerce.Entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;


@Embeddable
@Getter
@Setter
public class OrderedCartItem {
    private Long cartItemId;
    private Long productId;
    private String productName;
}
