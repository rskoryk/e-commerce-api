package projects.projectTwo.e_commerce.admin.service;

import projects.projectTwo.e_commerce.user.dto.UserResponse;

import java.util.List;

public interface AdminUserService {
    void blockUser(Long id);
    void unblockUser(Long id);
    List<UserResponse> getAllUsers();
    UserResponse getUserById(Long id);
}
