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
import com.ecommerce.Request.CartRequest;
import com.ecommerce.Response.CartItemResponse;
import com.ecommerce.Response.CartResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductsService productsService;

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
    @Transactional
    public CartResponse getCartContents(CustomUser user){
        ShoppingCart cart = user.getShoppingCart();

        if (cart == null || cart.getCartItems().isEmpty()){
            return new CartResponse("Cart is empty", Collections.emptyList(),0.0);
        }
        List<CartItemResponse> cartItemResponse = cart.getCartItems().stream()
                .map(CartItemResponse::new).collect(Collectors.toList());

        return new CartResponse("Cart details retrieved successfully", cartItemResponse, cart.getTotalPrice());
    }
    @Transactional
    public CartResponse addProductToCart(CartRequest cartRequest, CustomUser user) throws ProductNotFoundException {
        int quantity = cartRequest.getQuantity();
        Long productId = cartRequest.getProductId();

        ShoppingCart cart = user.getShoppingCart();
        if (cart == null) {
            cart = new ShoppingCart();
            cart.setUser(user);
            cart.setCartItems(new ArrayList<>());
            user.setShoppingCart(cart);
        }
        if (cart.getCartItems() == null) {
            cart.setCartItems(new ArrayList<>());
        }
        Optional<Products> optionalProduct = Optional.ofNullable(productsService.getProductByProductId(productId));
        if (optionalProduct.isPresent()) {
            Products product = optionalProduct.get();

            Optional<CartItems> cartProductOptional = cart.getCartItems().stream()
                    .filter(cp -> cp.getProduct().getProductId().equals(productId))
                    .findFirst();

            if (cartProductOptional.isPresent()) {
                CartItems cartItems = cartProductOptional.get();
                cartItems.setQuantity(cartItems.getQuantity() + quantity);
                cartItems.setPrice(product.getPrice() * cartItems.getQuantity());
            } else {
                CartItems cartItems = new CartItems();
                cartItems.setProduct(product);
                cartItems.setQuantity(quantity);
                cartItems.setPrice(product.getPrice() * quantity);
                cart.getCartItems().add(cartItems);
                cartItems.setShoppingCart(cart);
            }
            updateCartTotalPrice(cart);
            cartRepository.save(cart);

            CartResponse cartResponse = new CartResponse();
            cartResponse.setMessage("Product added to cart successfully");
            cartResponse.setCartItems(createCartItems(cart.getCartItems()));
            cartResponse.setTotalPrice(cart.getTotalPrice());

            return cartResponse;
        } else {
            throw new ProductNotFoundException("Product with ID " + productId + " not found");
        }
    }

    private void updateCartTotalPrice(ShoppingCart cart) {
        double totalPrice = cart.getCartItems().stream()
                .mapToDouble(CartItems::getPrice)
                .sum();
        cart.setTotalPrice(totalPrice);
    }

    private List<CartItemResponse> createCartItems(List<CartItems> cartItems) {
        return cartItems.stream()
                .map(cartItem -> new CartItemResponse(cartItem))
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
                if (cartItem.getProduct().getProductId().equals(productId)) {
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
        return cartRepository.findById(cartId).orElse(null);
    }

    @Override
    public ShoppingCart findCartByUserId(Long userId) {
        return userRepository.findById(userId)
                .map(CustomUser::getShoppingCart)
                .orElse(null);
    }

    @Override
    public ShoppingCart getCartForUser(CustomUser currentUser) {
        if (currentUser != null) {
            return currentUser.getShoppingCart();
        } else {
            throw new IllegalArgumentException("User cannot be null");
        }
    }
    @Override
    public void clearCart(CustomUser currentUser) {
        if (currentUser != null) {
            ShoppingCart shoppingCart = currentUser.getShoppingCart();
            if (shoppingCart != null) {
                shoppingCart.getCartItems().clear();
                shoppingCart.setTotalPrice(0);
                cartRepository.save(shoppingCart);
            }
        } else {
            throw new IllegalArgumentException("User cannot be null");
        }
    }
}
