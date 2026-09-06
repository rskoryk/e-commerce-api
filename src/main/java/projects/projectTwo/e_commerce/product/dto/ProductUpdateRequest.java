package projects.projectTwo.e_commerce.product.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductUpdateRequest {
    @Size(min = 2, max = 100)
    private String name;
    @Size(min = 10, max = 2000)
    private String description;
    private BigDecimal price;
    @Valid
    private CategoryRequest category;
    @Min(0)
    private Integer stock;
}
