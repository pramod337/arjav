package com.example.vajra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.vajra.model.EmailOTP;

import java.util.Optional;

@Repository
public interface EmailOtpRepository extends JpaRepository<EmailOTP, Long> {
	
    Optional<EmailOTP> findByEmail(String email);
    Optional<EmailOTP> findByOtp(int otp);  // This method is for searching by OTP
	void save(String toEmail);
}
