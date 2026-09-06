package projects.projectTwo.e_commerce.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import projects.projectTwo.e_commerce.common.exceptions.CustomIllegalStateException;
import projects.projectTwo.e_commerce.common.exceptions.ResourceAlreadyExistsException;
import projects.projectTwo.e_commerce.common.exceptions.ResourceNotFoundException;
import projects.projectTwo.e_commerce.product.dto.CategoryRequest;
import projects.projectTwo.e_commerce.product.dto.CategoryResponse;
import projects.projectTwo.e_commerce.product.entity.Category;
import projects.projectTwo.e_commerce.product.mapper.CategoryMapper;
import projects.projectTwo.e_commerce.product.repository.CategoryRepository;
import projects.projectTwo.e_commerce.product.repository.ProductRepository;
import projects.projectTwo.e_commerce.product.service.CategoryService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public CategoryResponse getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        return CategoryMapper.mapCategoryToCategoryDto(category);
    }

    @Override
    public List<CategoryResponse> getCategoriesByName(String name) {
        List<Category> category = categoryRepository.findAllByNameContainingIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        return category.stream().map(CategoryMapper::mapCategoryToCategoryDto).toList();
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

        if (productRepository.existsByCategory(category)) throw new CustomIllegalStateException("Cannot delete category that still has products assigned to it");

        categoryRepository.delete(category);

        return "Category removed successfully!";
    }

    @Override
    public String add(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) throw new ResourceAlreadyExistsException("Category already exists");

        Category category = CategoryMapper.mapCategoryRequestToCategory(request);

        try {
            categoryRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new ResourceAlreadyExistsException("Category already exists");
        }

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
