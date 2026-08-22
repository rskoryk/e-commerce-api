package projects.projectTwo.e_commerce.order.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderItemResponse {
    private String name;
    private BigDecimal price;
    private int quantity;
    private BigDecimal total;
}
