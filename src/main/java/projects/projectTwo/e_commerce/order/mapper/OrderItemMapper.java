package projects.projectTwo.e_commerce.order.mapper;

import projects.projectTwo.e_commerce.cart.entity.CartItem;
import projects.projectTwo.e_commerce.order.dto.OrderItemResponse;
import projects.projectTwo.e_commerce.order.entity.Order;
import projects.projectTwo.e_commerce.order.entity.OrderItem;

public class OrderItemMapper {
    public static OrderItemResponse mapOrderItemToOrderItemResponse(OrderItem orderItem){
        return OrderItemResponse.builder()
                .name(orderItem.getProduct().getName())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .total(orderItem.getTotal())
                .build();
    }

    public static OrderItem mapCartItemToOrderItem(CartItem cartItem, Order order){
        return OrderItem.builder()
                .product(cartItem.getProduct())
                .quantity(cartItem.getQuantity())
                .price(cartItem.getPrice())
                .total(cartItem.getTotal())
                .order(order)
                .build();
    }
}
