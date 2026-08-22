package projects.projectTwo.e_commerce.auth.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import projects.projectTwo.e_commerce.auth.dto.JwtResponse;
import projects.projectTwo.e_commerce.auth.dto.LoginRequest;
import projects.projectTwo.e_commerce.auth.dto.RegisterRequest;
import projects.projectTwo.e_commerce.auth.security.JwtService;
import projects.projectTwo.e_commerce.auth.service.AuthService;
import projects.projectTwo.e_commerce.common.exceptions.ResourceAlreadyExistsException;
import projects.projectTwo.e_commerce.common.exceptions.ResourceNotFoundException;
import projects.projectTwo.e_commerce.common.exceptions.TokenExpiredException;
import projects.projectTwo.e_commerce.user.entity.User;
import projects.projectTwo.e_commerce.user.enums.Role;
import projects.projectTwo.e_commerce.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    @Override
    public String register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) throw new ResourceAlreadyExistsException("Username already exists");
        if (userRepository.existsByEmail(request.getEmail())) throw new ResourceAlreadyExistsException("Email already exists");

        String verificationToken = UUID.randomUUID().toString();

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setEnabled(false);
        user.setVerificationToken(verificationToken);
        user.setVerificationTokenExpiration(LocalDateTime.now().plusHours(24));

        userRepository.save(user);

        emailService.sendVerificationEmail(request.getEmail(), verificationToken);

        return "Registration successful! Please check your email to verify your account.";
    }

    @Override
    public JwtResponse login(LoginRequest request) {
        User user = userRepository.findByUsernameOrEmail(request.getLogin(), request.getLogin())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        request.getPassword()
                )
        );

        String token = jwtService.generateToken(user.getUsername());

        return new JwtResponse(token, user.getUsername(), user.getRole().name());
    }

    @Override
    public String verify(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid verification token"));

        if (user.getVerificationTokenExpiration().isBefore(LocalDateTime.now())) throw new TokenExpiredException("Verification token expired");

        user.setEnabled(true);
        user.setVerificationToken(null);
        user.setVerificationTokenExpiration(null);

        userRepository.save(user);

        return "Email successfully verified! You can now log in.";
    }
}
