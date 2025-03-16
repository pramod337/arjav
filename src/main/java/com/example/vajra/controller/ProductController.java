package com.example.vajra.controller;

import com.example.vajra.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.example.vajra.model.Product;
import com.example.vajra.model.ProductWithVendorDTO;
import com.example.vajra.service.ProductService;
import com.example.vajra.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    // Fetch all products
    @GetMapping("/all")
    public List<ProductWithVendorDTO> getAllProductsWithVendor() {
        return productService.getAllProductsWithVendor();
    }

    // Fetch products for the logged-in vendor
    @GetMapping("/vendor")
    public List<Product> getProductsByLoggedInVendor() {
        User vendor = getAuthenticatedVendor();
        return productService.getProductsByVendor(vendor);
    }

    @GetMapping("/vendor/{vendorId}")
    public List<Product> getProductsByVendorId(@PathVariable Long vendorId) {
        return productService.getProductsByVendorId(vendorId);
    }
    
    
    //fetching details from name
    @GetMapping("/by-vendor")
    public ResponseEntity<List<ProductWithVendorDTO>> getProductsByVendorName(@RequestParam String vendorName) {
        List<ProductWithVendorDTO> products = productService.getProductsByVendorName(vendorName);
        return ResponseEntity.ok(products);
    }
    
    // Add a product for the logged-in vendor
    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        try {
            // Get authenticated vendor from the security context
            User vendor = getAuthenticatedVendor();
            return productService.addProduct(product, vendor);
        } catch (Exception e) {
            // Handle error with appropriate response
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: " + e.getMessage());
        }
    }

    // Helper method to get the vendor's ID from the username
    private Long getVendorIdByUsername(String username) {
        Optional<User> user = userService.findByUsername(username);
        
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vendor not found");
        }

        User vendor = user.get();
        
        if (!vendor.isVendor()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not a vendor");
        }

        return vendor.getId();  // Return the vendor's ID
    }

    // Helper method to get the logged-in vendor
    private User getAuthenticatedVendor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid authentication principal");
        }

        UserDetails userDetails = (UserDetails) principal;
        // Fetch vendor ID using the username
        Long vendorId = getVendorIdByUsername(userDetails.getUsername());

        // Fetch the vendor entity by vendor ID and return it
        return userService.getUserById(vendorId);
    }
    
    
    
}
