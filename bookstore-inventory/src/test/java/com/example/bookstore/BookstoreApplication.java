package com.example.bookstore;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.LocalDate;

@SpringBootApplication
public class BookstoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookstoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData(BookRepository repository) {
        return args -> {
            repository.save(new Book("Spring in Action", "Craig Walls", "Programming", 599.00, LocalDate.of(2023, 5, 10)));
            repository.save(new Book("Effective Java", "Joshua Bloch", "Programming", 799.00, LocalDate.of(2018, 1, 6)));
        };
    }
}
