package projects.projectTwo.e_commerce.product.service;

import projects.projectTwo.e_commerce.product.dto.ProductRequest;
import projects.projectTwo.e_commerce.product.dto.ProductResponse;

import java.util.List;

public interface ProductService {
    List<ProductResponse> getAllProducts();
    String createProduct(ProductRequest request);
    ProductResponse updateById(Long id, ProductRequest request);
    List<ProductResponse> getProductsByName(String name);
    List<ProductResponse> getProductsByCategory(String name);
    String deleteById(Long id);
}
