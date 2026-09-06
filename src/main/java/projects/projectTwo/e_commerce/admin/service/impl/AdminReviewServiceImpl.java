package projects.projectTwo.e_commerce.admin.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import projects.projectTwo.e_commerce.admin.service.AdminReviewService;
import projects.projectTwo.e_commerce.common.exceptions.ResourceNotFoundException;
import projects.projectTwo.e_commerce.review.entity.Review;
import projects.projectTwo.e_commerce.review.repository.ReviewRepository;

@Service
@RequiredArgsConstructor
public class AdminReviewServiceImpl implements AdminReviewService {

    private final ReviewRepository reviewRepository;

    private Review findReviewById(Long id){
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
    }

    @Override
    public void deleteReview(Long id) {
        Review review = findReviewById(id);
        reviewRepository.delete(review);
    }

    @Override
    public void deactivateReview(Long id) {
        Review review = findReviewById(id);
        review.setActive(false);
        reviewRepository.save(review);
    }

    @Override
    public void activateReview(Long id) {
        Review review = findReviewById(id);
        review.setActive(true);
        reviewRepository.save(review);
    }
}
