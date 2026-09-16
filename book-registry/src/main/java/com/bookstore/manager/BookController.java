package com.bookstore.manager;

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
// 1. DATA ENTITY MODEL (Creates database table)
// ==========================================
@Entity
@Table(name = "book_records")
class BookModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author;
    private Double price;

    // --- GETTERS AND SETTERS ---
    public Long getId() { 
        return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }

    public String getTitle() { 
        return title; 
    }
    public void setTitle(String title) { 
        this.title = title; 
    }

    public String getAuthor() { 
        return author; 
    }
    public void setAuthor(String author) { 
        this.author = author; 
    }

    public Double getPrice() { 
        return price; 
    }
    public void setPrice(Double price) { 
        this.price = price; 
    }
}

// ==========================================
// 2. DATA ACCESS REPOSITORY
// ==========================================
interface BookRepository extends JpaRepository<BookModel, Long> {
    // Automatically handles database queries: save(), findAll(), deleteById()
}

// ==========================================
// 3. MVC WEB CONTROLLER
// ==========================================
@Controller
public class BookController {

    private final BookRepository bookRepository;

    // Constructor Dependency Injection
    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // A. Form endpoint to collect book data
    @GetMapping("/book/new")
    public String showBookForm(Model model) {
        model.addAttribute("book", new BookModel());
        return "book-form";
    }

    // B. Receives form inputs and inserts straight into H2 Database
    @PostMapping("/book/save")
    public String saveBook(@ModelAttribute("book") BookModel book) {
        bookRepository.save(book);
        return "redirect:/books"; // Redirects browser to listing dashboard view
    }

    // C. Displays all dataset lines inside a custom dashboard data table
    @GetMapping("/books")
    public String listAllBooks(Model model) {
        List<BookModel> allBooks = bookRepository.findAll();
        model.addAttribute("bookList", allBooks);
        return "book-list";
    }
}
