package com.example.vajra.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.vajra.model.User;
import com.example.vajra.repository.EmailRepository;

@Service
public class LoginService {

    private final EmailRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginService(EmailRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean validateUserPassword(String username, String rawPassword) {
        // Validate input
        if (!StringUtils.hasText(username) || !StringUtils.hasText(rawPassword)) {
            throw new IllegalArgumentException("Username and password must not be empty");
        }

        // Fetch the user from the database
        Optional<User> user = userRepository.findByUsername(username);
        
        // Check if the user is present
        if (!user.isPresent()) {
            System.out.println("User not found: " + username); // Log the error
            return false;
        }

        // Extract the User object from Optional
        User foundUser = user.get();

        // Compare the provided password with the stored hashed password
        boolean isPasswordValid = passwordEncoder.matches(rawPassword, foundUser.getPassword());
        System.out.println("Password validation result for user " + username + ": " + isPasswordValid); // Log the result
        return isPasswordValid;
    }
}
