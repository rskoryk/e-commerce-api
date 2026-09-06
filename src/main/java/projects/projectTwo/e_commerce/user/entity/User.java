package projects.projectTwo.e_commerce.user.entity;

import jakarta.persistence.*;
import lombok.*;
import projects.projectTwo.e_commerce.order.entity.Order;
import projects.projectTwo.e_commerce.user.enums.Role;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"username"}),
    @UniqueConstraint(columnNames = {"email"})
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean emailVerified = false;
    private boolean blocked = false;
    private String verificationToken;
    private LocalDateTime verificationTokenExpiration;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;
}