package com.example.library.model;

public class Book {
    private String title;
    private String author;
    private String description;
    private double price;
    private String publishedDate;

    // Constructor
    public Book(String title, String author, String description, double price, String publishedDate) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.price = price;
        this.publishedDate = publishedDate;
    }

    // Getters
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getPublishedDate() { return publishedDate; }
}
