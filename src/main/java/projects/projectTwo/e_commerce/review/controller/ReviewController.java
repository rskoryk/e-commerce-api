package projects.projectTwo.e_commerce.review.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projects.projectTwo.e_commerce.review.dto.ReviewRequest;
import projects.projectTwo.e_commerce.review.dto.ReviewResponse;
import projects.projectTwo.e_commerce.review.dto.ReviewUpdateRequest;
import projects.projectTwo.e_commerce.review.service.ReviewService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponse> post(@Valid @RequestBody ReviewRequest request){
        return ResponseEntity.ok(reviewService.post(request));
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
}
