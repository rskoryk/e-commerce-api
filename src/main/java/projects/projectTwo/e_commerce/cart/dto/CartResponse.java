package projects.projectTwo.e_commerce.cart.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public class CartResponse {
    private List<CartItemResponse> cartItems;
    private BigDecimal total;
}
