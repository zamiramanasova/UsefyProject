package com.example.usefy.controller;

import com.example.usefy.model.User;
import com.example.usefy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public User register(@RequestBody UserRegistrationRequest request) {
        return userService.registerUser(
                request.getUsername(),
                request.getPassword(),
                request.getEmail()
        );
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {

        User user = userService.findByUsername(request.getUsername());

        if (user == null) {
            return "Invalid username or password";
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            return "Invalid username or password";
        }

        return "Login successful";
    }
}
