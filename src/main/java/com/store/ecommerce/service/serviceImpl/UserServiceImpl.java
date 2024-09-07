package com.store.ecommerce.service.serviceImpl;

import com.store.ecommerce.dto.UserDTO;
import com.store.ecommerce.entity.Category;
import com.store.ecommerce.entity.User;
import com.store.ecommerce.repository.UserRepository;
import com.store.ecommerce.service.UserService;
import com.store.ecommerce.utility.CategoryConverter;
import com.store.ecommerce.utility.UserConverter;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConverter userConverter;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDTO loginUser(UserDTO userDTO) {
        logger.info("Logging in User: {}", userDTO.getUsername());

        // Check if the username exists
        Optional<User> existingUser = userRepository.findByUsername(userDTO.getUsername());
        if (existingUser.isEmpty()) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        User user = existingUser.get();

        // Check password match
        if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        // Return the logged-in user DTO
        return userConverter.convertToDTO(user);
    }

    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        logger.info("Attempting to create user with username: {}", userDTO.getUsername());

        // Check if the username already exists
        Optional<User> existingUser = userRepository.findByUsername(userDTO.getUsername());
        if (existingUser.isPresent()) {
            logger.error("Failed to create user. Username '{}' already exists.", userDTO.getUsername());
            throw new IllegalArgumentException("Username '" + userDTO.getUsername() + "' already exists");
        }

        // Ensure password is not null before encoding
        if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        // Encrypt the password before saving
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        userDTO.setPassword(encodedPassword);

        // Set default role to USER if none provided
        if (userDTO.getRole() == null || userDTO.getRole().isEmpty()) {
            userDTO.setRole("USER");
            logger.info("No role provided. Assigning default role: USER");
        }

        // Save user only after ensuring username doesn't exist
        User user = userConverter.convertToEntity(userDTO);
        userRepository.save(user);
        logger.info("User '{}' created successfully with ID: {}", userDTO.getUsername(), user.getId());

        // Return the saved user DTO
        return userConverter.convertToDTO(user);
    }

    @Override
    public Optional<UserDTO> findUserByRole(String role) {
        return userRepository.findByRole(role).map(userConverter::convertToDTO);
    }
}