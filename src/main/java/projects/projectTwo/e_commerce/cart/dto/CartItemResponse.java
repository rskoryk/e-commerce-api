package projects.projectTwo.e_commerce.cart.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class CartItemResponse {
    private Long id;
    private String name;
    private int quantity;
    private BigDecimal price;
    private BigDecimal total;
}
