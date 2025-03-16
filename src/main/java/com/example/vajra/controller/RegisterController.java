package com.example.vajra.controller;

import com.example.vajra.model.User;
import com.example.vajra.service.UserService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
@CrossOrigin(origins = "http://localhost:3000")
public class RegisterController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody User user) {
        // Pass the isVendor value (default to false if not provided)
        Boolean isVendor = user.isVendor() != null ? user.isVendor() : false;
        userService.registerUser(user.getUsername(), user.getPassword(), user.getEmail(), isVendor, false);

        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully!");
        return ResponseEntity.ok(response);
    }
}