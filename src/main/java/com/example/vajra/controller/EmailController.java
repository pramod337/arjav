package com.example.vajra.controller;

import java.util.Optional;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.vajra.repository.EmailOtpRepository;
import com.example.vajra.repository.EmailRepository;
import com.example.vajra.model.EmailOTP;
import com.example.vajra.service.EmailSenderService;

import java.time.Duration;

import java.time.LocalDateTime;

@RestController
public class EmailController {
    
    @Autowired
    private EmailSenderService emailSenderService;
    
    @Autowired
    private EmailOtpRepository emailOtpRepository;
    
    @PostMapping("/send-email")
    public String sendEmail(@RequestParam String toEmail) {
        
        Random random = new Random();
        
        int randomInt = 1000 + random.nextInt(9000);
        
        String randomIntStr = String.valueOf(randomInt);
        
       String subject = "Please enter your OTP for login";
        
        // Send OTP email
        emailSenderService.SendEmail(toEmail, subject , randomIntStr);
        
        // Create a new EmailOtp entity
        EmailOTP emailOtp = new EmailOTP();
        emailOtp.setEmail(toEmail);
        emailOtp.setOtp(randomInt);
       // emailOtp.setCreatedAt(LocalDateTime.now());
        
        // Set expiration time (e.g., OTP expires in 5 minutes)
        emailOtp.setOtpGeneratedAt(LocalDateTime.now());
        
        // Save OTP and email to the database
        System.out.println("saving email to database");
        emailOtpRepository.save(emailOtp);
        
        return "Sent successfully";
    }
 
    // Method to verify OTP
    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String email, @RequestParam int otp) {
        Optional<EmailOTP> emailOtpOptional = emailOtpRepository.findByEmail(email);

        if (emailOtpOptional.isEmpty()) {
            return "OTP not found for the email.";
        }

        EmailOTP emailOtp = emailOtpOptional.get();

        // Check if OTP has expired (e.g., expiration time is 5 minutes)
        Duration duration = Duration.between(emailOtp.getOtpGeneratedAt(), LocalDateTime.now());
        if (duration.toMinutes() > 5) {
            return "OTP has expired.";
        }

        // Verify if the provided OTP matches the stored one
        if (emailOtp.getOtp() == otp) {
            return "OTP verified successfully.";
        } else {
            return "Invalid OTP.";
        }
    }
    
    
    
    
}
