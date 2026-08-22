package projects.projectTwo.e_commerce.user.entity;

import jakarta.persistence.*;
import lombok.*;
import projects.projectTwo.e_commerce.order.entity.Order;
import projects.projectTwo.e_commerce.user.enums.Role;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"username"}),
    @UniqueConstraint(columnNames = {"email"})
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean enabled = false;
    private String verificationToken;
    private LocalDateTime verificationTokenExpiration;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();
}