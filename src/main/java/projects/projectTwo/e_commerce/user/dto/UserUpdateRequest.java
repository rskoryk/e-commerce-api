package projects.projectTwo.e_commerce.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateRequest {
    @Size(min = 5, max = 30)
    private String username;
    @Email
    private String email;
    @Size(min = 6, max = 100)
    private String password;
}
