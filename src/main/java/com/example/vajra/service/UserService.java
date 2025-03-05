package com.example.vajra.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.vajra.model.User;
import com.example.vajra.repository.EmailRepository;
import com.example.vajra.model.User;


@Service
public class UserService {

    @Autowired
    private EmailRepository userRepository;
    private UserService User;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Register user with username, password, and email
    public User registerUser(String username, String password, String email) {
    	
        // Hash password before saving
        String encodedPassword = passwordEncoder.encode(password);  
        User user = new User(username, encodedPassword, email);  // Create user with hashed password
        return userRepository.save(user);  // Save user to the repository
    }
   
    
    
   
}
