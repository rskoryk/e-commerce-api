package projects.projectTwo.e_commerce.cart.mapper;

import projects.projectTwo.e_commerce.cart.dto.CartItemResponse;
import projects.projectTwo.e_commerce.cart.entity.CartItem;

public class CartItemMapper {
    public static CartItemResponse mapCartItemToCartItemResponse(CartItem cartItem){
        return CartItemResponse.builder()
                .name(cartItem.getProduct().getName())
                .price(cartItem.getPrice())
                .quantity(cartItem.getQuantity())
                .total(cartItem.getTotal())
                .build();
    }
}
