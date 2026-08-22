package projects.projectTwo.e_commerce.order.mapper;

import projects.projectTwo.e_commerce.order.dto.OrderResponse;
import projects.projectTwo.e_commerce.order.entity.Order;

public class OrderMapper {
    public static OrderResponse mapOrderToOrderResponse(Order order){
        return OrderResponse.builder()
                .orderId(order.getId())
                .orderItems(order.getOrderItems().stream().map(OrderItemMapper::mapOrderItemToOrderItemResponse).toList())
                .orderStatus(order.getStatus())
                .total(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
