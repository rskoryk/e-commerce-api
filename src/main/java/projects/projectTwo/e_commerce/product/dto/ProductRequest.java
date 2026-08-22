package projects.projectTwo.e_commerce.product.dto;

import lombok.Data;

@Data
public class ProductRequest {
    private String name;
    private String description;
    private Double price;
    private CategoryRequest category;
    private Integer stock;
}
