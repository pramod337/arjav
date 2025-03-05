package com.example.vajra.controller;

import com.example.vajra.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")  // Update the base path to avoid conflicts with Spring Security
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")  // Process login form submission
    public String validateCredentials(@RequestParam String username, @RequestParam String rawpassword) {
    	System.out.println("i reached login controller");
        if (loginService.validateUserPassword(username, rawpassword)) {
        	System.out.println("Im inside Login controller");
            return "redirect:/home.html";
        } else {
            return "redirect:/login?error=true";
        }
    }

    public class CustomErrorController implements ErrorController {

        @RequestMapping("/error")
        public ResponseEntity<String> handleError() {
            return ResponseEntity.ok("Custom Error Message");
        }
    }

    
}
