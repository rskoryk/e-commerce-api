package projects.projectTwo.e_commerce.cart.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import projects.projectTwo.e_commerce.cart.dto.AddCartItemRequest;
import projects.projectTwo.e_commerce.cart.dto.CartResponse;
import projects.projectTwo.e_commerce.cart.dto.UpdateCartItemRequest;
import projects.projectTwo.e_commerce.cart.entity.Cart;
import projects.projectTwo.e_commerce.cart.entity.CartItem;
import projects.projectTwo.e_commerce.cart.mapper.CartMapper;
import projects.projectTwo.e_commerce.cart.repository.CartRepository;
import projects.projectTwo.e_commerce.cart.service.CartService;
import projects.projectTwo.e_commerce.common.exceptions.InsufficientStockException;
import projects.projectTwo.e_commerce.common.exceptions.ResourceNotFoundException;
import projects.projectTwo.e_commerce.product.entity.Product;
import projects.projectTwo.e_commerce.product.repository.ProductRepository;
import projects.projectTwo.e_commerce.user.entity.User;
import projects.projectTwo.e_commerce.user.repository.UserRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CartServiceImpl implements CartService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(
                        Cart.builder()
                                .user(user)
                                .totalAmount(BigDecimal.ZERO)
                                .items(new ArrayList<>())
                                .build()
                ));
    }

    private void recalculateAndSave(Cart cart) {
        cart.setTotalAmount(cart.getItems().stream()
                .map(CartItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        cartRepository.save(cart);
    }

    @Override
    public void addItem(AddCartItemRequest request) {
        User user = getCurrentUser();
        Cart cart = getOrCreateCart(user);
        List<CartItem> cartItems = cart.getItems();
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        Optional<CartItem> existingItem = cartItems.stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()){
            CartItem cartItem = existingItem.get();
            int newQuantity = cartItem.getQuantity() + request.getQuantity();
            if (product.getStock() >= newQuantity) {
                cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
            } else throw new InsufficientStockException("Insufficient stock");
            cartItem.setTotal(product.getPrice().multiply(BigDecimal.valueOf(newQuantity)));
            recalculateAndSave(cart);
        } else {
            CartItem newCartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .price(product.getPrice())
                    .total(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())))
                    .build();
            cartItems.add(newCartItem);
            recalculateAndSave(cart);
        }
    }

    @Override
    public void removeItem(Long cartItemId) {
        User user = getCurrentUser();
        Cart cart = getOrCreateCart(user);
        List<CartItem> cartItems = cart.getItems();
        CartItem cartItem = cartItems.stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        cartItems.remove(cartItem);
        recalculateAndSave(cart);
    }

    @Override
    public CartResponse getCart() {
        User user = getCurrentUser();
        Cart cart = getOrCreateCart(user);
        return CartMapper.mapCartToCartResponse(cart);
    }

    @Override
    public void updateItem(Long cartItemId, UpdateCartItemRequest request) {
        User user = getCurrentUser();
        Cart cart = getOrCreateCart(user);
        int quantity = request.getQuantity();
        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        if (cartItem.getProduct().getStock() >= quantity) {
            cartItem.setQuantity(quantity);
            cartItem.setTotal(cartItem.getPrice().multiply(BigDecimal.valueOf(quantity)));
        } else throw new InsufficientStockException("Insufficient stock");
        recalculateAndSave(cart);
    }
}
