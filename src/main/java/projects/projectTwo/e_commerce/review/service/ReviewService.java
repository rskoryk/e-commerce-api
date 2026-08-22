package projects.projectTwo.e_commerce.review.service;

import projects.projectTwo.e_commerce.review.dto.ReviewRequest;
import projects.projectTwo.e_commerce.review.dto.ReviewResponse;
import projects.projectTwo.e_commerce.review.dto.ReviewUpdateRequest;

public interface ReviewService {
    ReviewResponse post(ReviewRequest request);
    ReviewResponse change(Long id, ReviewUpdateRequest updateRequest);
    void delete(Long id);
}
