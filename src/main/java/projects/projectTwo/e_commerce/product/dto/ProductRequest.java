package projects.projectTwo.e_commerce.product.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductRequest {
    @NotNull
    @Size(min = 2, max = 100)
    private String name;
    @NotNull
    @Size(min = 10, max = 2000)
    private String description;
    @NotNull
    private BigDecimal price;
    @NotNull
    @Valid
    private CategoryRequest category;
    @NotNull
    @Min(0)
    private Integer stock;
}
