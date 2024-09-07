package com.store.ecommerce.config;

import com.store.ecommerce.dto.UserDTO;
import com.store.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AdminInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        // Check if there is already an admin user in the system
        Optional<UserDTO> existingAdmin = userService.findUserByRole("ADMIN");

        if (existingAdmin.isEmpty()) {
            // Create the admin user if not found
            UserDTO adminUserDTO = new UserDTO();
            adminUserDTO.setUsername("admin");
            adminUserDTO.setPassword("admin");
            adminUserDTO.setRole("ADMIN");
            userService.registerUser(adminUserDTO);
            System.out.println("Admin user created!");
        } else {
            System.out.println("Admin user already exists.");
        }

        System.out.println("Application started successfully.");
    }
}
