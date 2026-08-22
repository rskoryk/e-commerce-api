package projects.projectTwo.e_commerce.product.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductResponse {
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private String category;
}
