package projects.projectTwo.e_commerce.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {
    @NotBlank
    private String login;
    @NotBlank
    private String password;
}
