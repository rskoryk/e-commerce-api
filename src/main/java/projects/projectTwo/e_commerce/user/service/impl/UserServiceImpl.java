package projects.projectTwo.e_commerce.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import projects.projectTwo.e_commerce.common.exceptions.ResourceAlreadyExistsException;
import projects.projectTwo.e_commerce.common.exceptions.ResourceNotFoundException;
import projects.projectTwo.e_commerce.user.dto.UserResponse;
import projects.projectTwo.e_commerce.user.dto.UserUpdateRequest;
import projects.projectTwo.e_commerce.user.entity.User;
import projects.projectTwo.e_commerce.user.mapper.UserMapper;
import projects.projectTwo.e_commerce.user.repository.UserRepository;
import projects.projectTwo.e_commerce.user.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public UserResponse getMe() {
        User user = getCurrentUser();
        return UserMapper.mapUserToUserResponse(user);
    }

    @Override
    public UserResponse updateMe(UserUpdateRequest request) {
        User user = getCurrentUser();
        String newUsername = request.getUsername();
        String newEmail = request.getEmail();
        String newPassword = request.getPassword();
        if (newUsername != null){
            if (userRepository.existsByUsername(newUsername)) {
                throw new ResourceAlreadyExistsException(String.format("User with username='%s' already exists", newUsername));
            }
            user.setUsername(newUsername);
        }
        if (newEmail != null) {
            if (userRepository.existsByEmail(newEmail)){
                throw new ResourceAlreadyExistsException(String.format("User with email='%s' already exists", newEmail));
            }
            user.setEmail(request.getEmail());
        }
        if (newPassword != null) user.setPassword(passwordEncoder.encode(request.getPassword()));

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e){
            throw new ResourceAlreadyExistsException("User with that username/email already exists");
        }

        return UserMapper.mapUserToUserResponse(user);
    }

    @Override
    public void deleteMe() {
        User user = getCurrentUser();
        userRepository.delete(user);
    }
}
