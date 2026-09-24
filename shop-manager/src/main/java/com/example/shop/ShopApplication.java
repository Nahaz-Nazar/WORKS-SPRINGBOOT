package com.example.shop;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.LocalDate;

@SpringBootApplication
public class ShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopApplication.class, args);
    }

    // ആപ്പ് റൺ ചെയ്യുമ്പോൾ തന്നെ 2 ഡാറ്റ ഓട്ടോമാറ്റിക് ആയി ആഡ് ചെയ്യും (ടെസ്റ്റ് ചെയ്യാൻ എളുപ്പത്തിന്)
    @Bean
    public CommandLineRunner demoData(ProductRepository repository) {
        return args -> {
            repository.save(new Product("Cycle", "Sports mountain terrain cycle", 12500.0, LocalDate.of(2030, 12, 31), "toy", 10));
            repository.save(new Product("Sugar", "Refined white sugar", 55.5, LocalDate.of(2027, 6, 15), "food", 50));
        };
    }
}
