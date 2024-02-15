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
        ShoppingCart shoppingCart = user.getShoppingCart();
        if (shoppingCart != null && shoppingCart.getCartItems() != null) {
            for (CartItems cartItem : shoppingCart.getCartItems()) {
                productsInCart.add(cartItem.getProducts());
            }
        }
        return productsInCart;
    }

    @Override
    public List<Products> addProductToCart(Long productId, String username) {
        CustomUser user = userRepository.findByUsername(username);

        Products productToAdd = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));

        ShoppingCart shoppingCart = user.getShoppingCart();
        if (shoppingCart == null) {
            shoppingCart = new ShoppingCart();
            shoppingCart.setUser(user);
        }

        List<CartItems> cartItemsList = shoppingCart.getCartItems();
        if (cartItemsList == null) {
            cartItemsList = new ArrayList<>();
            shoppingCart.setCartItems(cartItemsList);
        }

        Optional<CartItems> existingCartItemOptional = cartItemsList.stream()
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
            cartItemsList.add(cartItem);
        }
        userRepository.save(user);

        return cartItemsList.stream()
                .map(CartItems::getProducts)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProductFromCart(String username, Long productId) throws ProductNotFoundException {
        CustomUser user = userRepository.findByUsername(username);
        Products productToRemove = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + productId + " not found"));

        ShoppingCart userShoppingCart = user.getShoppingCart();
        if (userShoppingCart == null) {
            throw new ProductNotFoundException("Shopping cart not found for user");
        }

        CartItems cartItemToRemove = null;
        List<CartItems> cartItems = userShoppingCart.getCartItems();
        if (cartItems != null) {
            for (CartItems cartItem : cartItems) {
                if (cartItem.getProducts().getProductId().equals(productId)) {
                    cartItemToRemove = cartItem;
                    break;
                }
            }
        }

        if (cartItemToRemove != null) {
            cartItems.remove(cartItemToRemove);
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

    @Override
    public ShoppingCart getCartForUser(CustomUser currentUser) {
        return null;
    }

    @Override
    public void clearCart(CustomUser currentUser) {

    }
}


