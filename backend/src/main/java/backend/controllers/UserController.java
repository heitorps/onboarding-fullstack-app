package backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.models.User;
import backend.dtos.UserRegisterDTO;
import backend.dtos.UserResponseDTO;
import backend.services.UserService;
import jakarta.validation.Valid;

import java.util.Map;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        
        List<UserResponseDTO> users = userService.findAllUsers().stream()
            .map(u -> new UserResponseDTO(u.getId(), u.getUsername(), u.getBio()))
            .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(users);
    }


    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserRegisterDTO registerDTO){

        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(registerDTO.getPassword());

        User savedUser = userService.registerUser(user);

        UserResponseDTO responseDTO = new UserResponseDTO(
            savedUser.getId(),
            savedUser.getUsername(),
            savedUser.getBio()
        );

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<UserResponseDTO> updateProfile(
            @PathVariable Long id,
            @RequestBody Map<String,String> body){
        
        String newBio = body.get("bio");
        User updatedUser = userService.updateProfile(id, newBio);

        UserResponseDTO responseDTO = new UserResponseDTO(
            updatedUser.getId(),
            updatedUser.getUsername(),
            updatedUser.getBio()
        );

        return ResponseEntity.ok(responseDTO);
    }
    
    @PostMapping("/{followerId}/follow/{targetUserId}")
    public ResponseEntity<Map<String,String>> followUser(
            @PathVariable Long followerId,
            @PathVariable Long targetUserId){

        userService.followUser(followerId, targetUserId);

        return ResponseEntity.ok(Map.of("message", "Usuário seguido com sucesso."));
    }

    @PostMapping("/{followerId}/unfollow/{targetUserId}")
    public ResponseEntity<Map<String,String>> unfollowUser(
            @PathVariable Long followerId,
            @PathVariable Long targetUserId){

        userService.unfollowUser(followerId, targetUserId);

        return ResponseEntity.ok(Map.of("message", "Usuário deixado de seguir com sucesso."));
    }
}
