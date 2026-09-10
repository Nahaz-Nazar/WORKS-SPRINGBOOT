package com.example.MovieProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Controller
public class MovieProjectApplication {
	
	public static void main(String[] args) {
	    SpringApplication.run(MovieProjectApplication.class, args);
	}

	@GetMapping("/movie")
	public String movie(Model model) {

	    String title = "The Adventure";
	    String description = "A young hero begins an exciting journey to discover a mysterious world.";

	    boolean loggedIn = true;

	    model.addAttribute("title", title);
	    model.addAttribute("description", description);
	    model.addAttribute("loggedIn", loggedIn);

	    return "movie";
	}
}