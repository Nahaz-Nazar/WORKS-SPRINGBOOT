package com.example.auth.controller;

import com.example.auth.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class AuthController {

    // Simple in-memory user storage (Username -> User details)
    private static final Map<String, User> userDatabase = new HashMap<>();

    // 1. Registration Routes
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute("user") User user, Model model) {
        if (userDatabase.containsKey(user.getUsername())) {
            model.addAttribute("error", "Username already exists!");
            return "register";
        }
        userDatabase.put(user.getUsername(), user);
        return "redirect:/login?success=true";
    }

    // 2. Login Routes
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute("user") User user, HttpSession session, Model model) {
        User existingUser = userDatabase.get(user.getUsername());
        
        if (existingUser != null && existingUser.getPassword().equals(user.getPassword())) {
            // Save user profile state inside HTTP session
            session.setAttribute("loggedInUser", existingUser);
            return "redirect:/dashboard";
        }
        
        model.addAttribute("error", "Invalid username or password!");
        return "login";
    }

    // 3. Dashboard Route
    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login"; // Redirect to login if user isn't authenticated
        }
        model.addAttribute("user", loggedInUser);
        return "dashboard";
    }

    // 4. Logout Route
    @GetMapping("/logout")
    public String processLogout(HttpSession session) {
        session.invalidate(); // Destroy session
        return "redirect:/login";
    }
}
