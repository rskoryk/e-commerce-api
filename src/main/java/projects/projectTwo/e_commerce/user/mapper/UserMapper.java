package projects.projectTwo.e_commerce.user.mapper;

import projects.projectTwo.e_commerce.user.dto.UserResponse;
import projects.projectTwo.e_commerce.user.entity.User;

public class UserMapper {
    public static UserResponse mapUserToUserResponse(User user){
        return UserResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

}
