package projects.projectTwo.e_commerce.product.mapper;

import projects.projectTwo.e_commerce.product.dto.CategoryRequest;
import projects.projectTwo.e_commerce.product.dto.CategoryResponse;
import projects.projectTwo.e_commerce.product.entity.Category;

public class CategoryMapper {
    public static Category mapCategoryRequestToCategory(CategoryRequest request){
        return Category.builder()
                .name(request.getName())
                .build();
    }

    public static CategoryResponse mapCategoryToCategoryDto(Category category){
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
