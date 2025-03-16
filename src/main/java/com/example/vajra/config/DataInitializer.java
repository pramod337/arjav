package com.example.vajra.config;
import com.example.vajra.model.Role;
import com.example.vajra.repository.RoleRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;



@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if roles already exist in the database
        if (roleRepository.findByName("ROLE_VENDOR") == null) {
            roleRepository.save(new Role("ROLE_VENDOR"));
        }
        if (roleRepository.findByName("ROLE_ADMIN") == null) {
            roleRepository.save(new Role("ROLE_ADMIN"));
        }
        if (roleRepository.findByName("ROLE_CUSTOMER") == null) {
            roleRepository.save(new Role("ROLE_CUSTOMER"));
        }
    }
}