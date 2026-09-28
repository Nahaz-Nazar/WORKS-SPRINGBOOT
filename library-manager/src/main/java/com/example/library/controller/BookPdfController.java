package com.example.library.controller;

import com.example.library.model.Book;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class BookPdfController {

    // 1. Show the HTML page with the download button
    @GetMapping("/")
    public String showDownloadPage() {
        return "download"; // Maps to download.html
    }

    // 2. Generate and download the PDF
    @GetMapping("/generate-book-pdf")
    public void generateBookPdf(HttpServletResponse response) throws IOException {
        // Set up the response headers for downloading a PDF file
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=book_details.pdf");

        // Create default hardcoded book details as requested
        Book book = new Book(
            "Effective Java",
            "Joshua Bloch",
            "A comprehensive guide for best practices in the Java programming language.",
            450.00,
            "2018-05-08"
        );

        // Build the PDF Document
        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        // FIX: Correct way to generate safe fonts in OpenPDF using FontFactory
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLD);
        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD);
        Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL);

        // Add content to PDF
        document.add(new Paragraph("Library Book Details Report", titleFont));
        document.add(new Paragraph("------------------------------------------------------------------\n\n"));
        
        document.add(new Paragraph("Title: " + book.getTitle(), labelFont));
        document.add(new Paragraph("Author: " + book.getAuthor(), textFont));
        document.add(new Paragraph("Published Date: " + book.getPublishedDate(), textFont));
        document.add(new Paragraph("Price: INR " + book.getPrice(), textFont));
        document.add(new Paragraph("Description: " + book.getDescription(), textFont));

        document.close();
    }
}
