package projects.projectTwo.e_commerce.cart.mapper;

import projects.projectTwo.e_commerce.cart.dto.CartResponse;
import projects.projectTwo.e_commerce.cart.entity.Cart;

public class CartMapper {
    public static CartResponse mapCartToCartResponse(Cart cart){
        return CartResponse.builder()
                .cartItems(cart.getItems().stream().map(CartItemMapper::mapCartItemToCartItemResponse).toList())
                .total(cart.getTotalAmount())
                .build();
    }
}
