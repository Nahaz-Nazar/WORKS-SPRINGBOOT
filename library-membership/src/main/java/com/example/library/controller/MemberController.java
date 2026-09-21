package com.example.library.controller;

import com.example.library.model.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MemberController {

    // Shows the registration form
    @GetMapping("/register")
    public String showForm(Model model) {
        model.addAttribute("member", new Member());
        return "register";
    }

    // Handles form submission
    @PostMapping("/register")
    public String registerMember(@ModelAttribute("member") Member member, Model model) {
        model.addAttribute("memberName", member.getName());
        return "welcome";
    }
}
