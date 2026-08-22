package projects.projectTwo.e_commerce.auth.service;

import projects.projectTwo.e_commerce.auth.dto.JwtResponse;
import projects.projectTwo.e_commerce.auth.dto.LoginRequest;
import projects.projectTwo.e_commerce.auth.dto.RegisterRequest;

public interface AuthService {
    String register(RegisterRequest request);
    JwtResponse login(LoginRequest request);
    String verify(String token);
}
