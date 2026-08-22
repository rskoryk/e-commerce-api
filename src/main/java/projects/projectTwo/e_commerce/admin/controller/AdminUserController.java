package projects.projectTwo.e_commerce.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projects.projectTwo.e_commerce.admin.service.AdminUserService;
import projects.projectTwo.e_commerce.user.dto.UserResponse;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {
    private final AdminUserService adminUserService;

    @PutMapping("/{id}/block")
    public ResponseEntity<Void> blockUser(@PathVariable Long id){
        adminUserService.blockUser(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/unblock")
    public ResponseEntity<Void> unblockUser(@PathVariable Long id){
        adminUserService.unblockUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        return ResponseEntity.ok(adminUserService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(adminUserService.getUserById(id));
    }
}
