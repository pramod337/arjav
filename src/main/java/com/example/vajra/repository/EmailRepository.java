package com.example.vajra.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.vajra.model.EmailOTP;
import com.example.vajra.model.User;



import java.util.Optional;

@Repository
public interface EmailRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    
}
