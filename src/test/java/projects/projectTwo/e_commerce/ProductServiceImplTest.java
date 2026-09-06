package projects.projectTwo.e_commerce;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import projects.projectTwo.e_commerce.common.exceptions.ResourceAlreadyExistsException;
import projects.projectTwo.e_commerce.common.exceptions.ResourceNotFoundException;
import projects.projectTwo.e_commerce.product.dto.CategoryRequest;
import projects.projectTwo.e_commerce.product.dto.ProductRequest;
import projects.projectTwo.e_commerce.product.dto.ProductResponse;
import projects.projectTwo.e_commerce.product.dto.ProductUpdateRequest;
import projects.projectTwo.e_commerce.product.entity.Category;
import projects.projectTwo.e_commerce.product.entity.Product;
import projects.projectTwo.e_commerce.product.repository.CategoryRepository;
import projects.projectTwo.e_commerce.product.repository.ProductRepository;
import projects.projectTwo.e_commerce.product.service.impl.CategoryServiceImpl;
import projects.projectTwo.e_commerce.product.service.impl.ProductServiceImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ProductServiceImplTest {

    private ProductRepository productRepository;
    private ProductServiceImpl productService;
    private CategoryServiceImpl categoryService;
    private CategoryRepository categoryRepository;
    private ProductRequest productRequest;
    private Product product;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        categoryRepository = mock(CategoryRepository.class);
        productService = new ProductServiceImpl(productRepository, categoryRepository);
        categoryService = new CategoryServiceImpl(categoryRepository, productRepository);
        CategoryRequest categoryRequest = CategoryRequest.builder()
                .name("test123")
                .build();
        productRequest = ProductRequest.builder()
                .name("test123")
                .description("test123")
                .stock(1)
                .price(BigDecimal.valueOf(100))
                .category(categoryRequest)
                .build();
        product = Product.builder()
                .name("test123")
                .description("test123")
                .stock(1)
                .price(BigDecimal.valueOf(100))
                .category(Category.builder()
                        .name("test123")
                        .build())
                .build();
    }

    @Test
    public void testAddCategory_throwsException_whenRaceConditionOnSave(){
        CategoryRequest request = CategoryRequest.builder()
                .name("test")
                .build();

        when(categoryRepository.existsByName("test")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenThrow(new DataIntegrityViolationException(""));

        assertThrows(ResourceAlreadyExistsException.class, () -> categoryService.add(request));
    }

    @Test
    public void testCreateProduct_throwsException_whenRaceConditionOnSave(){
        Category category = Category.builder()
                .name("test")
                .build();

        when(categoryRepository.findByName("test")).thenReturn(Optional.of(category));

        ProductRequest request = ProductRequest.builder()
                .name("test")
                .description("test")
                .stock(1)
                .price(BigDecimal.valueOf(100))
                .category(CategoryRequest.builder()
                        .name("test")
                        .build())
                .build();

        when(productRepository.existsByName("test")).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenThrow(new DataIntegrityViolationException(""));

        assertThrows(ResourceAlreadyExistsException.class, () -> productService.createProduct(request));
    }

    @Test
    public void testGetAllProducts_successfullyReturnsAllProducts(){
        List<Product> products = List.of(product);

        when(productRepository.findAll()).thenReturn(products);

        List<ProductResponse> result = productService.getAllProducts();

        assertEquals(1, result.size());
        assertEquals(products.getFirst().getName(), result.getFirst().getName());
    }

    @Test
    public void testCreateProduct_successfullyCreatesProduct(){
        Category category = Category.builder()
                .name("test123")
                .build();
        when(categoryRepository.findByName("test123")).thenReturn(Optional.of(category));

        productService.createProduct(productRequest);

        verify(productRepository).save(argThat(product ->
                product.getName().equals(productRequest.getName()) &&
                product.getDescription().equals(productRequest.getDescription()) &&
                product.getStock().equals(productRequest.getStock()) &&
                product.getPrice().equals(productRequest.getPrice()) &&
                product.getCategory().getName().equals(productRequest.getCategory().getName())
        ));
    }

    @Test
    public void testCreateProduct_throwsException_whenProductAlreadyExists(){
        when(productRepository.existsByName(productRequest.getName())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> productService.createProduct(productRequest));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    public void testUpdateById_successfullyUpdatesProduct(){
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.existsByNameAndIdNot(product.getName(), 1L)).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductUpdateRequest updateRequest = ProductUpdateRequest.builder()
                .description("test12345")
                .build();

        productService.updateById(1L, updateRequest);

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void testDeleteById_throwsException_whenProductNotFound(){
        when(productRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> productService.deleteById(1L));
        verify(productRepository, never()).deleteById(1L);
    }
}
