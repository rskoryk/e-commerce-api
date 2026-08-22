package projects.projectTwo.e_commerce.order.service;

import projects.projectTwo.e_commerce.order.dto.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse checkout();
    void cancelOrder(Long orderId);
    List<OrderResponse> getMyOrders();
    OrderResponse getOrderById(Long orderId);
}
