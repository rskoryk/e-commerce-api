package projects.projectTwo.e_commerce;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import projects.projectTwo.e_commerce.auth.dto.LoginRequest;
import projects.projectTwo.e_commerce.auth.dto.RegisterRequest;
import projects.projectTwo.e_commerce.auth.security.JwtService;
import projects.projectTwo.e_commerce.auth.service.impl.AuthServiceImpl;
import projects.projectTwo.e_commerce.auth.service.impl.EmailService;
import projects.projectTwo.e_commerce.common.exceptions.ResourceAlreadyExistsException;
import projects.projectTwo.e_commerce.common.exceptions.ResourceNotFoundException;
import projects.projectTwo.e_commerce.user.entity.User;
import projects.projectTwo.e_commerce.user.enums.Role;
import projects.projectTwo.e_commerce.user.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceImplTest {

    private UserRepository userRepository;
    private AuthServiceImpl authService;
    private JwtService jwtService;
    private EmailService emailService;
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtService = mock(JwtService.class);
        authenticationManager = mock(AuthenticationManager.class);
        emailService = mock(EmailService.class);
        authService = new AuthServiceImpl(userRepository, passwordEncoder, jwtService, authenticationManager, emailService);

        User user = User.builder()
                .id(1L)
                .username("testuser")
                .build();

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        registerRequest = RegisterRequest.builder()
                .username("testuser123")
                .email("testuser123@gmail.com")
                .password("test123")
                .build();
        loginRequest = LoginRequest.builder()
                .login("testuser123")
                .password("test123")
                .build();
    }

    @Test
    public void testRegistration_successfullyRegistersUser() {
        when(userRepository.existsByUsername("testuser123")).thenReturn(false);
        when(userRepository.existsByEmail("testuser123@gmail.com")).thenReturn(false);

        authService.register(registerRequest);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        verify(emailService, times(1)).sendVerificationEmail(eq("testuser123@gmail.com"), any(String.class));
        verify(userRepository).save(captor.capture());

        User savedUser = captor.getValue();

        assertEquals(registerRequest.getUsername(), savedUser.getUsername());
        assertEquals(registerRequest.getEmail(), savedUser.getEmail());
        assertEquals(Role.USER, savedUser.getRole());
        assertFalse(savedUser.isEmailVerified());
    }

    @Test
    public void testRegistration_throwsException_whenRaceConditionOnSave(){
        when(userRepository.existsByUsername("testuser123")).thenReturn(false);
        when(userRepository.existsByEmail("testuser123@gmail.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenThrow(new DataIntegrityViolationException(""));

        assertThrows(ResourceAlreadyExistsException.class, () -> authService.register(registerRequest));
    }

    @Test
    public void testRegistration_throwsException_whenUsernameExists(){
        when(userRepository.existsByUsername("testuser123")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> authService.register(registerRequest));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testRegistration_throwsException_whenEmailExists(){
        when(userRepository.existsByEmail("testuser123@gmail.com")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> authService.register(registerRequest));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testLogin_successfullyLoginsUser(){
        User user = User.builder()
                .username("testuser123")
                .email("testuser123@gmail.com")
                .password("test123")
                .role(Role.USER)
                .build();

        when(userRepository.findByUsernameOrEmail(loginRequest.getLogin(), loginRequest.getLogin())).thenReturn(Optional.of(user));

        authService.login(loginRequest);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).generateToken(user.getUsername());
    }

    @Test
    public void testLogin_throwsException_whenUserNotFound(){
        when(userRepository.findByUsernameOrEmail(loginRequest.getLogin(), loginRequest.getLogin())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authService.login(loginRequest));
    }
}
