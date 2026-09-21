package com.example.jobportal.controller;

import com.example.jobportal.model.UserRegistration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    // Displays the empty registration form
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistration());
        return "register";
    }

    // Handles the form submission and displays the success screen
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserRegistration user, Model model) {
        // Business logic like validation or saving to database goes here
        model.addAttribute("name", user.getFullName());
        return "success";
    }
}
