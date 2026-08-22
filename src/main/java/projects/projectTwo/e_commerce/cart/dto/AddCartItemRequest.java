package projects.projectTwo.e_commerce.cart.dto;

import lombok.Data;

@Data
public class AddCartItemRequest {
    private Long productId;
    private int quantity;
}
