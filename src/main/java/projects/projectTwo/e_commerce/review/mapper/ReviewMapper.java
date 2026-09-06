package projects.projectTwo.e_commerce.review.mapper;

import projects.projectTwo.e_commerce.review.dto.ReviewResponse;
import projects.projectTwo.e_commerce.review.entity.Review;

public class ReviewMapper {
    public static ReviewResponse mapReviewToReviewResponse(Review review){
        return ReviewResponse.builder()
                .id(review.getId())
                .username(review.getUser().getUsername())
                .content(review.getContent())
                .userId(review.getUser().getId())
                .rating(review.getRating())
                .build();
    }
}
