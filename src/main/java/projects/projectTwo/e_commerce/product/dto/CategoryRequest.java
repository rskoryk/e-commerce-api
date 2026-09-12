package projects.projectTwo.e_commerce.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryRequest {
    @NotBlank
    @Size(min = 2, max = 50)
    private String name;
}
