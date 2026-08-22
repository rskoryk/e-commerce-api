package projects.projectTwo.e_commerce.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projects.projectTwo.e_commerce.product.dto.CategoryRequest;
import projects.projectTwo.e_commerce.product.dto.CategoryResponse;
import projects.projectTwo.e_commerce.product.service.CategoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id){
        return ResponseEntity.ok(categoryService.getById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<CategoryResponse> getCategoriesByName(@RequestParam String name){
        return ResponseEntity.ok(categoryService.getCategoryByName(name));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id){
        return ResponseEntity.ok(categoryService.deleteById(id));
    }

    @PostMapping
    public ResponseEntity<String> addCategory(@Valid @RequestBody CategoryRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.add(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateById(@PathVariable Long id, @RequestBody CategoryRequest request){
        return ResponseEntity.ok(categoryService.update(id, request));
    }
}
