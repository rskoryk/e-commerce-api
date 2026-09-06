package projects.projectTwo.e_commerce.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddCartItemRequest {
    @NotNull
    private Long productId;
    @Min(1)
    private int quantity;
}
