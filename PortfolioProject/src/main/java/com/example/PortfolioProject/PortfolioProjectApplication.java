package com.example.PortfolioProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Controller
public class PortfolioProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(PortfolioProjectApplication.class, args);
    }

    @GetMapping("/start")
    public String start() {
        return "redirect:/portfolio";
    }

    @GetMapping("/portfolio")
    public String portfolio() {
        return "portfolio";
    }
}