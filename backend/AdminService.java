package com.example.E.learning.Platform.Service;

import com.example.E.learning.Platform.Model.Admin;
import com.example.E.learning.Platform.Repository.AdminRepository;
import com.example.E.learning.Platform.SecurityConfiguration.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public void registerAdmin(Admin admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        adminRepository.save(admin);
    }
}
