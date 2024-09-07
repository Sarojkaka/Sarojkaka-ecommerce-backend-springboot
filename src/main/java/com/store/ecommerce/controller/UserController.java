package com.store.ecommerce.controller;

import com.store.ecommerce.dto.UserDTO;
import com.store.ecommerce.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    // User registration endpoint
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@ModelAttribute UserDTO userDTO) {
        logger.info("Received request to register user: {}", userDTO.getUsername());

        // Call service to handle user registration
        UserDTO savedUser = userService.registerUser(userDTO);

        logger.info("Received request to register user with details: {}", userDTO);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    // User login endpoint
    @PostMapping("/login")
    public ResponseEntity<UserDTO> loginUser(@ModelAttribute UserDTO userDTO) {
        logger.info("Received request to login user: {}", userDTO.getUsername());

        // Call service to handle user login
        UserDTO loggedInUser = userService.loginUser(userDTO);

        if (loggedInUser != null) {
            logger.info("Login Success");
            return new ResponseEntity<>(loggedInUser, HttpStatus.OK);
        } else {
            logger.error("Invalid username or password");
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }
}
