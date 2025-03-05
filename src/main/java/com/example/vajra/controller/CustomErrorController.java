package com.example.vajra.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(WebRequest request, Model model) {
        // Get the status code and error message from the request attributes
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code", RequestAttributes.SCOPE_REQUEST);
        String errorMessage = (String) request.getAttribute("javax.servlet.error.message", RequestAttributes.SCOPE_REQUEST);

        // Add the error details to the model
        model.addAttribute("status", statusCode);
        model.addAttribute("error", errorMessage);

        return "error"; // Return the error.html template
    }

    // No need to implement getErrorPath() in Spring Boot 3.x
}