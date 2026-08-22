package projects.projectTwo.e_commerce.user.dto;

import lombok.Builder;
import lombok.Data;
import projects.projectTwo.e_commerce.user.enums.Role;

@Data
@Builder
public class UserResponse {
    private String username;
    private String email;
    private Role role;
}
