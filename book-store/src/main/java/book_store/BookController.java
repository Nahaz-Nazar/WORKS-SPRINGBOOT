package book_store;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// 1. DATA MODEL FOR A BOOK
class Book {
    private String title;
    private String author;
    private double price;

    public Book(String title, String author, double price) {
        this.title = title;
        this.author = author;
        this.price = price;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public double getPrice() { return price; }
}

// 2. CONTROLLER MAPPINGS
@Controller
public class BookController {

    @GetMapping("/home")
    public String getHome() {
        return "home";
    }

    @GetMapping("/books")
    public String getBooks(Model model) {
        Book favoriteBook = new Book("The Great Gatsby", "F. Scott Fitzgerald", 499.00);
        model.addAttribute("book", favoriteBook);
        return "books";
    }
}
