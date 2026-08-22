package projects.projectTwo.e_commerce.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projects.projectTwo.e_commerce.order.entity.Order;
import projects.projectTwo.e_commerce.user.entity.User;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUser(User user);
}
