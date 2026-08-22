package projects.projectTwo.e_commerce.user.service;

import projects.projectTwo.e_commerce.user.dto.UserResponse;
import projects.projectTwo.e_commerce.user.dto.UserUpdateRequest;

public interface UserService {
    UserResponse getMe();
    UserResponse updateMe(UserUpdateRequest request);
    void deleteMe();
}
