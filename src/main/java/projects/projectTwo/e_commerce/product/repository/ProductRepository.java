package projects.projectTwo.e_commerce.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projects.projectTwo.e_commerce.product.entity.Category;
import projects.projectTwo.e_commerce.product.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
    List<Product> findAllByName(String name);
    List<Product> findAllByCategory(Category category);
}
