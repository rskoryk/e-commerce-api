package projects.projectTwo.e_commerce.product.mapper;

import projects.projectTwo.e_commerce.product.dto.CategoryRequest;
import projects.projectTwo.e_commerce.product.dto.CategoryResponse;
import projects.projectTwo.e_commerce.product.entity.Category;

public class CategoryMapper {
    public static Category mapCategoryDtoToCategory(CategoryRequest dto){
        return Category.builder()
                .name(dto.getName())
                .build();
    }

    public static CategoryResponse mapCategoryToCategoryDto(Category category){
        return CategoryResponse.builder()
                .name(category.getName())
                .build();
    }
}
