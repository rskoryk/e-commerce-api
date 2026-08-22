package projects.projectTwo.e_commerce.product.service;

import projects.projectTwo.e_commerce.product.dto.CategoryRequest;
import projects.projectTwo.e_commerce.product.dto.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse getById(Long id);
    CategoryResponse getCategoryByName(String name);
    List<CategoryResponse> getAllCategories();
    String deleteById(Long id);
    String add(CategoryRequest request);
    CategoryResponse update(Long id, CategoryRequest request);
}
