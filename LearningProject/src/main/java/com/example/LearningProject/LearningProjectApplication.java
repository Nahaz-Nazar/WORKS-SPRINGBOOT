package com.example.LearningProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Controller
public class LearningProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearningProjectApplication.class, args);
    }

    @GetMapping("/home")
    public String home(Model model) {

        String welcomeMessage = "Learning is a journey, not a destination.";
        String headingMessage = "<h1>Hello from Spring Boot!</h1>";
        boolean loggedIn = true;

        model.addAttribute("welcomeMessage", welcomeMessage);
        model.addAttribute("headingMessage", headingMessage);
        model.addAttribute("loggedIn", loggedIn);

        return "home";
    }
}