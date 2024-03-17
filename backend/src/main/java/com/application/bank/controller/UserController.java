package com.application.bank.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.application.bank.DTO.ResponseDTO;
import com.application.bank.DTO.UserDTO;
import com.application.bank.model.User;
import com.application.bank.service.UserService;
import com.application.bank.util.JwtUtil;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        // Validate userDTO
        if (userService.findByUsername(userDTO.getUsername()) != null) {
            return ResponseEntity.badRequest().body(ResponseDTO.error("Username is already taken!"));
        }

        if (userService.findByEmail(userDTO.getEmail()) != null) {
            return ResponseEntity.badRequest().body(ResponseDTO.error("Email is already registered!"));
        }

        String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());

        // Create a new user
        User newUser = new User();
        newUser.setUsername(userDTO.getUsername());
        newUser.setPassword(encryptedPassword);
        newUser.setEmail(userDTO.getEmail());

        userService.save(newUser);

        return ResponseEntity.ok(ResponseDTO.success("User registered successfully!", null));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserDTO userDTO) {
        User user = userService.findByEmail(userDTO.getEmail());

        if (user == null) {
            return ResponseEntity.badRequest().body(ResponseDTO.error("User is not found!"));

        }

        if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body(ResponseDTO.error("Invalid email or password!"));
        }

        String token = jwtUtil.generateToken(user);

        return ResponseEntity.ok(ResponseDTO.success("Logined successfully!", token));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();

        Optional<User> user = userService.findById(UUID.fromString(userId));

        if (user.isPresent() == false) {
            return ResponseEntity.badRequest().body(ResponseDTO.error("Error occouring"));
        }

        return ResponseEntity.ok(ResponseDTO.success(null, user.get()));
    }
}
