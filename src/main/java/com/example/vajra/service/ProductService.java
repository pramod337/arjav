package com.example.vajra.service;

import java.sql.Timestamp; // Correct import
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.vajra.model.Product;
import com.example.vajra.model.ProductWithVendorDTO;
import com.example.vajra.model.User;
import com.example.vajra.model.VendorDTO;
import com.example.vajra.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<ProductWithVendorDTO> getAllProductsWithVendor() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> {
                    User vendor = product.getVendor();
                    VendorDTO vendorDTO = (vendor != null) 
                            ? new VendorDTO(vendor.getId(), vendor.getUsername(), vendor.getEmail())
                            : null; // or handle it appropriately
                    return new ProductWithVendorDTO(
                            product.getId(),
                            product.getName(),
                            product.getDescription(),
                            product.getPrice(),
                            product.getCreatedAt(),
                            product.getUpdatedAt(),
                            vendorDTO
                    );
                })
                .collect(Collectors.toList());
    }

    public Product addProduct(Product product, User vendor) {
        product.setVendor(vendor);
        product.setCreatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

    public List<Product> getProductsByVendor(User vendor) {
        return productRepository.findByVendor(vendor);
    }

    public List<Product> getProductsByVendorId(Long vendorId) {
        return productRepository.findByVendorId(vendorId);
    }

    public List<ProductWithVendorDTO> getProductsByVendorName(String vendorName) {
        // Fetch the raw results from the repository
        List<Object[]> results = productRepository.findByVendorNameWithVendorDetails(vendorName);

        // Map the raw results to ProductWithVendorDTO
        return results.stream()
                .map(this::mapToProductWithVendorDTO)
                .collect(Collectors.toList());
    }

    private ProductWithVendorDTO mapToProductWithVendorDTO(Object[] result) {
        return new ProductWithVendorDTO(
                ((Number) result[0]).longValue(), // productId
                (String) result[1],              // productName
                (String) result[2],              // productDescription
                ((Number) result[3]).doubleValue(), // productPrice
                ((Timestamp) result[4]).toLocalDateTime(), // productCreatedAt
                result[5] != null ? ((Timestamp) result[5]).toLocalDateTime() : null, // productUpdatedAt
                new VendorDTO(
                        ((Number) result[6]).longValue(), // vendorId
                        (String) result[7],              // vendorUsername
                        (String) result[8]               // vendorEmail
                )
        );
    }
}