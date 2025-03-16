package com.example.vajra.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.vajra.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{
Optional<Role> findByName(String name);
}
