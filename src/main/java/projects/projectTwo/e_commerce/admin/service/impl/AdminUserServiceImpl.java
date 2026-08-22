package projects.projectTwo.e_commerce.admin.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import projects.projectTwo.e_commerce.admin.service.AdminUserService;
import projects.projectTwo.e_commerce.common.exceptions.ResourceNotFoundException;
import projects.projectTwo.e_commerce.user.dto.UserResponse;
import projects.projectTwo.e_commerce.user.entity.User;
import projects.projectTwo.e_commerce.user.mapper.UserMapper;
import projects.projectTwo.e_commerce.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;

    private User findUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public void blockUser(Long id) {
        User user = findUserById(id);
        user.setEnabled(false);
        userRepository.save(user);
    }

    @Override
    public void unblockUser(Long id) {
        User user = findUserById(id);
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserMapper::mapUserToUserResponse).toList();
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = findUserById(id);
        return UserMapper.mapUserToUserResponse(user);
    }
}
