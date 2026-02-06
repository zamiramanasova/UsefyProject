package com.example.usefy.web;

import com.example.usefy.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class WebAuthController {

    private final UserService userService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(
            @Valid @ModelAttribute("registrationForm") RegistrationForm form,
            BindingResult bindingResult,
            Model model
    ) {

        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            userService.registerUser(form.getUsername(), form.getPassword(), form.getEmail());
        } catch (IllegalArgumentException ex) {
            // например, логин уже существует
            bindingResult.rejectValue("username", "error.username", ex.getMessage());
            return "register";
        }

        return "redirect:/login?registered";
    }

    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "registered", required = false) String registered,
                                Model model) {

        if (error != null) model.addAttribute("error", "Неверный логин или пароль");
        if (registered != null) model.addAttribute("message", "Регистрация прошла успешно — войдите.");
        return "login";
    }

}