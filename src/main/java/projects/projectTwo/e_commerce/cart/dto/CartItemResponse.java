package projects.projectTwo.e_commerce.cart.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public class CartItemResponse {
    private String name;
    private int quantity;
    private BigDecimal price;
    private BigDecimal total;
}
