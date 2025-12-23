package com.example.usefy.controller;

import com.example.usefy.model.User;
import com.example.usefy.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody UserRegistrationRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        User user = userService.registerUser(
                request.getUsername(),
                request.getPassword(),
                request.getEmail()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
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
