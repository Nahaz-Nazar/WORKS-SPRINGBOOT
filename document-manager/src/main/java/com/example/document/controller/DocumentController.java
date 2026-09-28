package com.example.document.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class DocumentController {

    @Autowired
    private JavaMailSender mailSender;

    // Define the directory where files will be stored
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    // 1. Show the upload page
    @GetMapping("/")
    public String showUploadForm() {
        return "upload"; // Maps to upload.html
    }

    // 2. Handle the file upload and send confirmation email
    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a file to upload.");
            return "upload";
        }

        try {
            // Ensure the upload directory exists
            File uploadFolder = new File(UPLOAD_DIR);
            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs();
            }

            // Save the file locally
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
            Files.write(path, bytes);

            // Send confirmation email
            sendConfirmationEmail(file.getOriginalFilename());

            model.addAttribute("message", "File '" + file.getOriginalFilename() + "' uploaded successfully! Confirmation email sent.");
        } catch (IOException e) {
            model.addAttribute("message", "Failed to upload file: " + e.getMessage());
        }

        return "upload";
    }

    // Helper method to send a quick plain-text email
    private void sendConfirmationEmail(String fileName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@company.com");
        message.setTo("test-inbox@mailtrap.io"); // Replace with your fixed recipient email
        message.setSubject("Document Uploaded Successfully");
        message.setText("Hello,\n\nThis is to confirm that the file '" + fileName + "' has been successfully uploaded to the system.");
        
        mailSender.send(message);
    }
}
