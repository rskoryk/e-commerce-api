package projects.projectTwo.e_commerce.admin.service;

public interface AdminReviewService {
    void deleteReview(Long id);
    void deactivateReview(Long id);
    void activateReview(Long id);
}
