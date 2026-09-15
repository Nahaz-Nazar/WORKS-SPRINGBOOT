package training_registration;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import java.lang.annotation.*;

// ==========================================
// 1. CUSTOM VALIDATION FOR GMAIL ONLY
// ==========================================
@Documented
@Constraint(validatedBy = GmailValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@interface GmailOnly {
    String message() default "Email must be a valid Gmail address (must contain @gmail.com)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

class GmailValidator implements ConstraintValidator<GmailOnly, String> {
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.trim().isEmpty()) return false;
        return email.toLowerCase().contains("@gmail.com");
    }
}

// ==========================================
// 2. CUSTOM VALIDATION FOR INDIA ADDRESS
// ==========================================
@Documented
@Constraint(validatedBy = AddressValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@interface Address {
    String message() default "Address must include the word 'India'";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

class AddressValidator implements ConstraintValidator<Address, String> {
    @Override
    public boolean isValid(String address, ConstraintValidatorContext context) {
        if (address == null || address.trim().isEmpty()) return false;
        return address.toLowerCase().contains("india");
    }
}

// ==========================================
// 3. REGISTRATION FORM MODEL (DTO)
// ==========================================
class RegistrationForm {
    
    @NotBlank(message = "Name must not be blank")
    private String name;

    @GmailOnly
    private String email;

    @Address
    private String address;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}

// ==========================================
// 4. THE WEB CONTROLLER
// ==========================================
@Controller
public class RegistrationController {

    @GetMapping("/register")
    public String showForm(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        return "register-form";
    }

    @PostMapping("/register")
    public String processRegistration(@Valid @ModelAttribute("registrationForm") RegistrationForm form, 
                                      BindingResult result) {
        if (result.hasErrors()) {
            return "register-form"; 
        }
        return "success"; 
    }
}
