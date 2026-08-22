package projects.projectTwo.e_commerce.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projects.projectTwo.e_commerce.product.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
    boolean existsByName(String name);
}
