package com.example.usefy.controller;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class WelcomePage {

    @GetMapping("/")
    public String printWelcome(ModelMap model) {
        List<String> messages = new ArrayList<>();
        messages.add("Welcome to");
        messages.add("our big school");
        messages.add("find here your lesson!");
        model.addAttribute("messages", messages);
        return messages.toString();
    }

}
