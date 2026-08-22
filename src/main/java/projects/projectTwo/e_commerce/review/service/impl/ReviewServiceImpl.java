package projects.projectTwo.e_commerce.review.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import projects.projectTwo.e_commerce.common.exceptions.CustomAccessDeniedException;
import projects.projectTwo.e_commerce.common.exceptions.ResourceNotFoundException;
import projects.projectTwo.e_commerce.product.entity.Product;
import projects.projectTwo.e_commerce.product.repository.ProductRepository;
import projects.projectTwo.e_commerce.review.dto.ReviewRequest;
import projects.projectTwo.e_commerce.review.dto.ReviewResponse;
import projects.projectTwo.e_commerce.review.dto.ReviewUpdateRequest;
import projects.projectTwo.e_commerce.review.entity.Review;
import projects.projectTwo.e_commerce.review.mapper.ReviewMapper;
import projects.projectTwo.e_commerce.review.repository.ReviewRepository;
import projects.projectTwo.e_commerce.review.service.ReviewService;
import projects.projectTwo.e_commerce.user.entity.User;
import projects.projectTwo.e_commerce.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    private Review findReviewByIdAndCheckAccess(Long id){
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        if (!review.getUser().getId().equals(getCurrentUser().getId())) throw new CustomAccessDeniedException("Access denied");
        return review;
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public ReviewResponse post(ReviewRequest request) {
        User user = getCurrentUser();
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        Review review = Review.builder()
                .content(request.getContent())
                .rating(request.getRating())
                .product(product)
                .user(user)
                .active(true)
                .build();
        reviewRepository.save(review);
        return ReviewMapper.mapReviewToReviewResponse(review);
    }

    @Override
    public ReviewResponse change(Long id, ReviewUpdateRequest updateRequest) {
        Review review = findReviewByIdAndCheckAccess(id);
        review.setContent(updateRequest.getContent());
        review.setRating(updateRequest.getRating());
        reviewRepository.save(review);
        return ReviewMapper.mapReviewToReviewResponse(review);
    }

    @Override
    public void delete(Long id) {
        Review review = findReviewByIdAndCheckAccess(id);
        reviewRepository.delete(review);
    }
}
