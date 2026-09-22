package com.example.portal.controller;

import com.example.portal.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    private static final Map<String, User> userRegistry = new HashMap<>();

    // 1. User Registration Form (Sign Up Page)
    @GetMapping("/signup")
    public String showSignUpForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String processSignUp(@ModelAttribute("user") User user) {
        userRegistry.put(user.getEmail(), user);
        return "redirect:/login";
    }

    // 2. Simple Login Routes to bridge Registration and Welcome Page
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam("email") String email, 
                               @RequestParam("password") String password, 
                               HttpSession session) {
        User matchedUser = userRegistry.get(email);
        if (matchedUser != null && matchedUser.getPassword().equals(password)) {
            session.setAttribute("currentUser", matchedUser);
            return "redirect:/welcome"; // Redirect user to a Welcome Page
        }
        return "login";
    }

    // 3. Welcome Page
    @GetMapping("/welcome")
    public String showWelcomePage(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", currentUser);
        return "welcome";
    }

    // 4. Logout Link functionality
    @GetMapping("/logout")
    public String processLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
