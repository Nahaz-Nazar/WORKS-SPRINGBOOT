package grocery_store;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Arrays;
import java.util.List;

class Product {
    private int id;
    private String name;
    private double price;

    public Product(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
}

@Controller
public class ProductController {

    @GetMapping("/single-product")
    public String getSingleProduct(Model model) {
        Product sugar = new Product(101, "Sugar", 55.5);
        model.addAttribute("product", sugar);
        return "single-product";
    }

    @GetMapping("/product-list")
    public String getProductList(Model model) {
        List<Product> products = Arrays.asList(
            new Product(101, "Sugar", 55.5),
            new Product(102, "Salt", 20.0),
            new Product(103, "Wheat Flour", 38.75)
        );
        model.addAttribute("products", products);
        return "product-list";
    }
}
