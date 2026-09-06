package projects.projectTwo.e_commerce.product.mapper;

import projects.projectTwo.e_commerce.product.dto.ProductRequest;
import projects.projectTwo.e_commerce.product.dto.ProductResponse;
import projects.projectTwo.e_commerce.product.entity.Category;
import projects.projectTwo.e_commerce.product.entity.Product;

public class ProductMapper {
    public static ProductResponse mapProductToProductResponse(Product product){
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .category(product.getCategory().getName())
                .price(product.getPrice())
                .stock(product.getStock())
                .build();
    }

    public static Product mapProductRequestToProduct(ProductRequest request, Category category){
        return Product.builder()
                .name(request.getName())
                .category(category)
                .price(request.getPrice())
                .description(request.getDescription())
                .stock(request.getStock())
                .build();
    }
}
