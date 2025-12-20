package com.example.usefy.web;

import com.example.usefy.model.User;
import com.example.usefy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {

        User user = userService.findByUsername(principal.getName());

        model.addAttribute("username", user.getUsername());
        model.addAttribute("user", user);

        return "profile";
    }
}
