package projects.projectTwo.e_commerce.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projects.projectTwo.e_commerce.cart.entity.Cart;
import projects.projectTwo.e_commerce.user.entity.User;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
