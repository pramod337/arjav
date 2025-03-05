package com.example.vajra.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "email_otp")
public class EmailOTP {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private int otp;

    @Column(name = "otp_generated_at", nullable = true)
    private LocalDateTime otpGeneratedAt;

    @Column(name = "expiration_time", nullable = false)
    private LocalDateTime expirationTime;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getOtp() {
        return otp;
    }

    public void setOtp(int otp) {
        this.otp = otp;
    }

    public LocalDateTime getOtpGeneratedAt() {
        return otpGeneratedAt;
    }

    public void setOtpGeneratedAt(LocalDateTime otpGeneratedAt) {
        this.otpGeneratedAt = otpGeneratedAt;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    @PrePersist
    public void prePersist() {
        if (expirationTime == null) {
            expirationTime = LocalDateTime.now().plusMinutes(10); // Example: OTP valid for 10 minutes
        }
    }


    // Utility method to set expiration time automatically
    public void updateExpirationTime() {
        if (this.otpGeneratedAt != null) {
            this.expirationTime = this.otpGeneratedAt.plusMinutes(5);
        }
    }
}
