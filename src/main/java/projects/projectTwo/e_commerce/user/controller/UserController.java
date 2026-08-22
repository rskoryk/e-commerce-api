package projects.projectTwo.e_commerce.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projects.projectTwo.e_commerce.user.dto.UserResponse;
import projects.projectTwo.e_commerce.user.dto.UserUpdateRequest;
import projects.projectTwo.e_commerce.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(){
        return ResponseEntity.ok(userService.getMe());
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMe(@Valid @RequestBody UserUpdateRequest request){
        return ResponseEntity.ok(userService.updateMe(request));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMe(){
        userService.deleteMe();
        return ResponseEntity.noContent().build();
    }
}
