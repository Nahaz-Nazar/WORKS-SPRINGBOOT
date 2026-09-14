package school_project;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String getHome() {
        return "home";
    }

    @GetMapping("/product-details")
    public String getProductDetails() {
        return "product-details";
    }
}
