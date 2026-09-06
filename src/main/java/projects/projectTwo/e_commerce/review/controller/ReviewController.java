package projects.projectTwo.e_commerce.review.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projects.projectTwo.e_commerce.review.dto.ReviewRequest;
import projects.projectTwo.e_commerce.review.dto.ReviewResponse;
import projects.projectTwo.e_commerce.review.dto.ReviewUpdateRequest;
import projects.projectTwo.e_commerce.review.service.ReviewService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponse> post(@Valid @RequestBody ReviewRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.post(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponse> change(@PathVariable Long id, @Valid @RequestBody ReviewUpdateRequest updateRequest){
        return ResponseEntity.ok(reviewService.change(id, updateRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByProductId(@PathVariable Long productId){
        return ResponseEntity.ok(reviewService.getReviewsByProductId(productId));
    }
}
