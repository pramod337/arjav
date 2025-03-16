package com.example.vajra.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.vajra.model.User;
import com.example.vajra.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@PostMapping("/register")
	public User registeruser(@RequestBody User user) {
		return userService.registerUser(
				user.getUsername(),
				user.getPassword(),
				user.getEmail(),
				user.isVendor(),
				false);
	}
	
	
	@GetMapping("/vendors")
	public List<User> getAllVendors(){
	return userService.getAllVendors();
	}
	
}
