package com.ecommerce.Service;

import com.ecommerce.Entity.CartItems;
import com.ecommerce.Entity.CustomUser;
import com.ecommerce.Entity.Products;
import com.ecommerce.Entity.ShoppingCart;
import com.ecommerce.ExceptionHandler.ProductNotFoundException;
import com.ecommerce.ExceptionHandler.UserNotFoundException;
import com.ecommerce.Repository.CartRepository;
import com.ecommerce.Repository.ProductRepository;
import com.ecommerce.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service @Transactional
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;

    private CustomUser getCurrentUser() throws UserNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof CustomUser) {
            return (CustomUser) authentication.getPrincipal();
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    @Override
    public List<Products> getCartContents() throws UserNotFoundException {
        CustomUser user = getCurrentUser();
        Hibernate.initialize(user.getShoppingCart());

        List<Products> productsInCart = new ArrayList<>();
        for (ShoppingCart shoppingCart : user.getShoppingCart()) {
            if (shoppingCart.getCartItems() != null) {
                for (CartItems cartItem : shoppingCart.getCartItems()) {
                    productsInCart.add(cartItem.getProducts());
                }
            }
        }
        return productsInCart;
    }

    @Override
    public List<Products> addProductToCart(Long productId, String username) {
        CustomUser user = userRepository.findByUsername(username);

        Products productToAdd = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));

        ShoppingCart shoppingCart = user.getShoppingCart()
                .stream()
                .findFirst()
                .orElse(new ShoppingCart());

        if (shoppingCart.getCartItems() == null) {
            shoppingCart.setCartItems(new ArrayList<>());
        }

        Optional<CartItems> existingCartItemOptional = shoppingCart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProducts().getProductId().equals(productId))
                .findFirst();

        if (existingCartItemOptional.isPresent()) {
            CartItems existingCartItem = existingCartItemOptional.get();
            existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
            existingCartItem.setPrice(existingCartItem.getProducts().getPrice() * existingCartItem.getQuantity());
        } else {
            CartItems cartItem = new CartItems();
            cartItem.setShoppingCart(shoppingCart);
            cartItem.setProducts(productToAdd);
            cartItem.setQuantity(1);
            cartItem.setPrice(productToAdd.getPrice());
            cartItem.setPurchased(false);
            cartItem.setOrder(null);
            shoppingCart.getCartItems().add(cartItem);
        }
        if (user.getShoppingCart().isEmpty()) {
            user.getShoppingCart().add(shoppingCart);
        }
        userRepository.save(user);

        return user.getShoppingCart().stream()
                .flatMap(cart -> cart.getCartItems().stream().map(CartItems::getProducts))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProductFromCart(String username, Long productId) throws ProductNotFoundException {
        CustomUser user = userRepository.findByUsername(username);
        Products productToRemove = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + productId + " not found"));

        ShoppingCart userShoppingCart = user.getShoppingCart()
                .stream()
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Shopping cart not found for user"));

        CartItems cartItemToRemove = null;
        for (CartItems cartItem : userShoppingCart.getCartItems()) {
            if (cartItem.getProducts().getProductId().equals(productId)) {
                cartItemToRemove = cartItem;
                break;
            }
        }
        if (cartItemToRemove != null) {
            userShoppingCart.getCartItems().remove(cartItemToRemove);
            cartRepository.save(userShoppingCart);
        }
    }

    @Override
    public void saveCart(ShoppingCart shoppingCart) {
    }

    @Override
    public ShoppingCart findCartByCartId(Long cartId) {
        return cartRepository.findById(cartId)
                .orElse(null);
    }

    @Override
    public ShoppingCart findCartByUserId(Long userId) {
        return (ShoppingCart) userRepository.findById(userId)
                .map(CustomUser::getShoppingCart)
                .orElse(null);
    }
}
