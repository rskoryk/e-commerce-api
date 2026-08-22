package projects.projectTwo.e_commerce.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import projects.projectTwo.e_commerce.common.exceptions.ResourceAlreadyExistsException;
import projects.projectTwo.e_commerce.common.exceptions.ResourceNotFoundException;
import projects.projectTwo.e_commerce.product.dto.CategoryRequest;
import projects.projectTwo.e_commerce.product.dto.CategoryResponse;
import projects.projectTwo.e_commerce.product.entity.Category;
import projects.projectTwo.e_commerce.product.entity.Product;
import projects.projectTwo.e_commerce.product.mapper.CategoryMapper;
import projects.projectTwo.e_commerce.product.repository.CategoryRepository;
import projects.projectTwo.e_commerce.product.service.CategoryService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponse getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        return CategoryMapper.mapCategoryToCategoryDto(category);
    }

    @Override
    public CategoryResponse getCategoryByName(String name) {
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        return CategoryMapper.mapCategoryToCategoryDto(category);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(CategoryMapper::mapCategoryToCategoryDto).toList();
    }

    @Override
    public String deleteById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        categoryRepository.delete(category);

        return "Category removed successfully!";
    }

    @Override
    public String add(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) throw new ResourceAlreadyExistsException("Category already exists");

        Category category = CategoryMapper.mapCategoryDtoToCategory(request);

        categoryRepository.save(category);

        return "Category added successfully!";
    }

    @Override
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        category.setName(request.getName());
        Category updatedCategory = categoryRepository.save(category);
        return CategoryMapper.mapCategoryToCategoryDto(updatedCategory);
    }
}
