package com.example.vajra.service;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.vajra.model.Role;
import com.example.vajra.model.User;
import com.example.vajra.repository.EmailRepository;
import com.example.vajra.repository.RoleRepository;

@Service
public class UserService {

    @Autowired
    private EmailRepository userRepository; // Repository for user operations

    @Autowired
    private RoleRepository roleRepository; // Repository for role operations

    @Autowired
    private PasswordEncoder passwordEncoder; // Password encoder


    public User registerUser(String username, String password, String email, Boolean isVendor, Boolean isAdmin) {

        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists: " + username);
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already exists: " + email);
        }


        String encodedPassword = passwordEncoder.encode(password);


        User user = new User(username, encodedPassword, email);
        user.setCreatedAt(LocalDateTime.now());

        // **Explicitly set isVendor (default to false if null)**
        user.setIsVendor(isVendor != null ? isVendor : false);

        // Assign the roles based on the flags for Vendor and Admin
        if (isVendor != null && isVendor == true) {
            Role vendorRole = roleRepository.findByName("ROLE_VENDOR")
                    .orElseThrow(() -> new RuntimeException("Role not found: ROLE_VENDOR"));
            user.getRoles().add(vendorRole);
        }

        
        user.setIsAdmin(isAdmin != null ? isAdmin : false);
        
        if (isAdmin != null && isAdmin) {
            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("Role not found: ROLE_ADMIN"));
            user.getRoles().add(adminRole);
        }

        // If the user is neither Vendor nor Admin, assign ROLE_USER
        if ((isVendor == null || !isVendor) && (isAdmin == null || !isAdmin)) {
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Role not found: ROLE_USER"));
            user.getRoles().add(userRole);
        }

        // Save the user to the database
        return userRepository.save(user);
    }


    // Get all vendors
    public List<User> getAllVendors() {
        return userRepository.findByIsVendor(true);
    }

    // Get a user by ID
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    // Find a user by username
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Find a user by email
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
   
    
    
}
