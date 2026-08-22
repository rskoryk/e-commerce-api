package projects.projectTwo.e_commerce.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import projects.projectTwo.e_commerce.common.exceptions.ResourceAlreadyExistsException;
import projects.projectTwo.e_commerce.common.exceptions.ResourceNotFoundException;
import projects.projectTwo.e_commerce.product.dto.ProductRequest;
import projects.projectTwo.e_commerce.product.dto.ProductResponse;
import projects.projectTwo.e_commerce.product.entity.Category;
import projects.projectTwo.e_commerce.product.entity.Product;
import projects.projectTwo.e_commerce.product.mapper.CategoryMapper;
import projects.projectTwo.e_commerce.product.mapper.ProductMapper;
import projects.projectTwo.e_commerce.product.repository.CategoryRepository;
import projects.projectTwo.e_commerce.product.repository.ProductRepository;
import projects.projectTwo.e_commerce.product.service.ProductService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(ProductMapper::mapProductToProductResponse).toList();
    }

    @Override
    public String createProduct(ProductRequest request) {
        if (productRepository.existsByName(request.getName())) {
            throw new ResourceAlreadyExistsException(String.format("Product with name='%s' already exists", request.getName()));
        }
        Product product = ProductMapper.mapProductRequestToProduct(request);

        productRepository.save(product);

        return "Product successfully added!";
    }

    @Override
    public ProductResponse updateById(Long id, ProductRequest request) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Product with name='%d' not found", id))
        );

        if (productRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new ResourceAlreadyExistsException(String.format("Product with name='%s' already exists", request.getName()));
        }

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategory(CategoryMapper.mapCategoryDtoToCategory(request.getCategory()));

        Product saved = productRepository.save(product);

        return ProductMapper.mapProductToProductResponse(saved);
    }

    @Override
    public List<ProductResponse> getProductsByName(String name) {
        List<Product> products = productRepository.findAllByName(name);
        return products.stream().map(ProductMapper::mapProductToProductResponse).toList();
    }

    @Override
    public List<ProductResponse> getProductsByCategory(String name) {
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        List<Product> products = productRepository.findAllByCategory(category);
        return products.stream().map(ProductMapper::mapProductToProductResponse).toList();
    }

    @Override
    public String deleteById(Long id) {
        if (!productRepository.existsById(id)){
            throw new ResourceNotFoundException(String.format("Product with id='%d' not found", id));
        }

        productRepository.deleteById(id);

        return "Product successfully removed!";
    }
}
