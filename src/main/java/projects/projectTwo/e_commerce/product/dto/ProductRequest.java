package projects.projectTwo.e_commerce.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductRequest {
    @NotNull
    @Size(min = 2, max = 100)
    private String name;
    @NotNull
    @Size(min = 10, max = 2000)
    private String description;
    @NotNull
    private Double price;
    @NotNull
    private CategoryRequest category;
    @NotNull
    @Min(0)
    private Integer stock;
}
