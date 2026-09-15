package user_form_app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FormController {

    // 1. Display the initial input form page
    @GetMapping("/register")
    public String showForm() {
        return "register";
    }

    // 2. Handle form submission using the GET method
    @GetMapping("/submit-form")
    public String processingForm(@RequestParam("username") String username, Model model) {
        // Send individual user data
        model.addAttribute("username", username);
        
        // Simulating the full raw received parameter data mapping
        String fullFormData = "username=" + username;
        model.addAttribute("fullData", fullFormData);
        
        return "result";
    }
}
