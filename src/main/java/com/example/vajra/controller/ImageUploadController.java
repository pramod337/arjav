package com.example.vajra.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api")
public class ImageUploadController {

    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/upload-image")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) {
        try {
            // Create the upload directory if it doesn't exist
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                System.out.println("Created upload directory: " + uploadPath.toAbsolutePath());
            }

            // Save the file
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);
            System.out.println("File saved to: " + filePath.toAbsolutePath());

            // Return the image URL
            String imageUrl = "/uploads/" + fileName;
            return ResponseEntity.ok().body(new UploadResponse(true, imageUrl));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new UploadResponse(false, null));
        }
    }

    private static class UploadResponse {
        private boolean success;
        private String imageUrl;

        public UploadResponse(boolean success, String imageUrl) {
            this.success = success;
            this.imageUrl = imageUrl;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }
}