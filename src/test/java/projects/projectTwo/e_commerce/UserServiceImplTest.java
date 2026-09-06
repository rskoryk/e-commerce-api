package projects.projectTwo.e_commerce;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import projects.projectTwo.e_commerce.common.exceptions.ResourceAlreadyExistsException;
import projects.projectTwo.e_commerce.user.dto.UserUpdateRequest;
import projects.projectTwo.e_commerce.user.entity.User;
import projects.projectTwo.e_commerce.user.enums.Role;
import projects.projectTwo.e_commerce.user.repository.UserRepository;
import projects.projectTwo.e_commerce.user.service.impl.UserServiceImpl;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    private UserServiceImpl userService;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private SecurityContext securityContext;
    private Authentication authentication;
    private User user;


    @BeforeEach
    void setUp(){
        securityContext = mock(SecurityContext.class);
        authentication = mock(Authentication.class);
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userRepository, passwordEncoder);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser123");

        SecurityContextHolder.setContext(securityContext);

        user = User.builder()
                .username("testuser123")
                .email("testuser123@gmail.com")
                .password("test")
                .orders(new ArrayList<>())
                .emailVerified(true)
                .blocked(false)
                .role(Role.USER)
                .build();

        when(userRepository.findByUsername("testuser123")).thenReturn(Optional.of(user));
    }

    @Test
    public void testUpdateMe_successfullyUpdatesMe(){
        UserUpdateRequest updateRequest = UserUpdateRequest.builder()
                .username("newuser123")
                .email("newuser123@gmail.com")
                .password("new_password")
                .build();
        when(userRepository.existsByUsername(updateRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(updateRequest.getEmail())).thenReturn(false);

        userService.updateMe(updateRequest);

        verify(userRepository, times(1)).save(any(User.class));
        assertEquals("newuser123", user.getUsername());
        assertEquals("newuser123@gmail.com", user.getEmail());
    }

    @Test
    public void testUpdateMe_throwsException_whenRaceConditionOnSave(){
        UserUpdateRequest updateRequest = UserUpdateRequest.builder()
                .username("newuser123")
                .email("newuser123@gmail.com")
                .password("new_password")
                .build();

        when(userRepository.existsByUsername(updateRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(updateRequest.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenThrow(new DataIntegrityViolationException(""));

        assertThrows(ResourceAlreadyExistsException.class, () -> userService.updateMe(updateRequest));
    }
}
