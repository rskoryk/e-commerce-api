package projects.projectTwo.e_commerce.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projects.projectTwo.e_commerce.product.dto.ProductRequest;
import projects.projectTwo.e_commerce.product.dto.ProductResponse;
import projects.projectTwo.e_commerce.product.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@Valid @RequestBody ProductRequest request){
        return new ResponseEntity<>(productService.createProduct(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest request){
        ProductResponse product = productService.updateById(id, request);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/search/by-name")
    public ResponseEntity<List<ProductResponse>> getProductsByName(@RequestParam String name){
        return ResponseEntity.ok(productService.getProductsByName(name));
    }

    @GetMapping("/search/by-category")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@RequestParam String name){
        return ResponseEntity.ok(productService.getProductsByCategory(name));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
        return ResponseEntity.ok(productService.deleteById(id));
    }
}
