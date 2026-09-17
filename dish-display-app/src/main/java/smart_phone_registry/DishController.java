package smart_phone_registry;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DishController {

    // Extracts variables directly from the dynamic URL path structure (/dish/{name}/{price})
    @GetMapping("/dish/{name}/{price}")
    public String displayDynamicDish(
            @PathVariable("name") String dishName,
            @PathVariable("price") Double dishPrice,
            Model model) {
        
        // Pass values securely to the Thymeleaf template view model
        model.addAttribute("name", dishName);
        model.addAttribute("price", dishPrice);
        
        return "dish-view"; // Looks for templates/dish-view.html
    }
}
