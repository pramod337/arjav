package com.example.vajra.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.example.vajra.model.User;
import com.example.vajra.repository.EmailRepository;

@RestController
@RequestMapping("/vendor/auth")
public class VendorloginController {

    private final EmailRepository emailRepository;

    @Autowired
    public VendorloginController(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    @GetMapping("/check-vendor")
    public ResponseEntity<?> checkVendor() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("User is not authenticated");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> user = emailRepository.findByUsername(userDetails.getUsername());

        System.out.println("hello worlddddd---------------------------------------------------------------------------------------");
        
        if (user.isPresent()) {
            boolean isVendor = user.get().isVendor();
            if (isVendor) {
                // Redirect to add_products.html if the user is a vendor
            	 System.out.println("worlddddd---------------------------------------------------------------------------------------");
                 
                RedirectView redirectView = new RedirectView();
                redirectView.setUrl("/add_products.html");
                return new ResponseEntity<>(redirectView, HttpStatus.OK);
            }
            return ResponseEntity.status(403).body("User is not a vendor");
        }

        return ResponseEntity.status(404).body("User not found");
    }
}
