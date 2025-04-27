package com.example.E.learning.Platform.Repository;

import com.example.E.learning.Platform.Model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    public Optional<Admin> findAdminByUsername(String username);
}
