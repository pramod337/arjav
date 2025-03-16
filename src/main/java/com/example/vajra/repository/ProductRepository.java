package com.example.vajra.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.vajra.model.Product;
import com.example.vajra.model.ProductWithVendorDTO;
import com.example.vajra.model.User;
import com.example.vajra.model.VendorDTO;

public interface ProductRepository extends JpaRepository<Product, Long>{

	List<Product> findByVendor(User vendor);
	List<Product> findAll();


	@Query("SELECT p FROM Product p WHERE p.vendor.id = :vendorId")
    List<Product> findByVendorId(@Param("vendorId") Long vendorId);
	
	@Query(value = "SELECT p.id AS productId, p.name AS productName, p.description AS productDescription, p.price AS productPrice, " +
	        "p.created_at AS productCreatedAt, p.updated_at AS productUpdatedAt, " +
	        "v.id AS vendorId, v.username AS vendorUsername, v.email AS vendorEmail " +
	        "FROM product p JOIN vajra v ON p.vendor_id = v.id " +
	        "WHERE v.username = :vendorName AND v.is_vendor = 1", nativeQuery = true)
	List<Object[]> findByVendorNameWithVendorDetails(@Param("vendorName") String vendorName);
}
