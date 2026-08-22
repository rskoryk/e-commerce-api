package projects.projectTwo.e_commerce.order.dto;

import lombok.Builder;
import lombok.Data;
import projects.projectTwo.e_commerce.order.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResponse {
    private Long orderId;
    private List<OrderItemResponse> orderItems;
    private BigDecimal total;
    private OrderStatus orderStatus;
    private LocalDateTime createdAt;
}
