package projects.projectTwo.e_commerce.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projects.projectTwo.e_commerce.review.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
