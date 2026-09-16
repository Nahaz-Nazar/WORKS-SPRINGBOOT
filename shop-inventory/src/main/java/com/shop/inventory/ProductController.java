package com.shop.inventory;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;

// ==========================================
// 1. PRODUCT MODEL (Creates Database Table)
// ==========================================
@Entity
@Table(name = "shop_products")
class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double price;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}

// ==========================================
// 2. DATA ACCESS LAYER REPOSITORY
// ==========================================
interface ProductRepository extends JpaRepository<Product, Long> {
}

// ==========================================
// 3. PRODUCT WEB CONTROLLER
// ==========================================
@Controller
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Displays product entry collection forms
    @GetMapping("/product/new")
    public String showProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "product-form";
    }

    // Handles form data writes straight to the repository database table
    @PostMapping("/product/save")
    public String saveProduct(@ModelAttribute("product") Product product) {
        productRepository.save(product);
        return "redirect:/products";
    }

    // Fetches saved dataset and maps values into the UI table dashboard
    @GetMapping("/products")
    public String listAllProducts(Model model) {
        List<Product> allProducts = productRepository.findAll();
        model.addAttribute("productList", allProducts);
        return "product-list";
    }
}
