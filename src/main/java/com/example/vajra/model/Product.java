// created new file now

package com.example.vajra.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String description;
	private double price;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	 @Column(name = "image_url")
	    private String imageUrl; // New field for image URL
	
	
	@ManyToOne
    @JoinColumn(name = "vendor_id")
	//@JsonIgnore
    private User vendor;
	
	
	public User getVendor() {
		return vendor;
	}
	
	public void setVendor(User vendor) {
		
	this.vendor=vendor;	
		
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId() {
		this.id=id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

	
}
