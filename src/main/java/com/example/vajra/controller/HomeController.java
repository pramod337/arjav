package com.example.vajra.controller;

import com.example.vajra.model.User;
import com.example.vajra.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.vajra.model.User;

@Controller
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")
public class HomeController {

	@Autowired
    private UserService userService;

	
	@GetMapping("/home")
			public String home() {
		return "home";
		
	}
	
	//@PostMapping("/register")
	//public ResponseEntity<String> registerUser(@RequestBody User user) {
      //  userService.registerUser(user.getUsername(), user.getPassword(), user.getEmail());
        //return ResponseEntity.ok("User registered successfully!");
    //}
}
