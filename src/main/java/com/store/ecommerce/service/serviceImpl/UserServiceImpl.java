package com.store.ecommerce.service.serviceImpl;

import com.store.ecommerce.dto.UserDTO;
import com.store.ecommerce.entity.User;
import com.store.ecommerce.exception.ResourceNotFoundException;
import com.store.ecommerce.repository.UserRepository;
import com.store.ecommerce.service.UserService;
import com.store.ecommerce.utility.UserConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    private final UserConverter userConverter;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserConverter userConverter) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
    }

    @Override
    public UserDTO loginUser(UserDTO userDTO) {
        logger.info("Logging in User: {}", userDTO.getUsername());

        // Retrieve user from the repository
        Optional<User> existingUser = userRepository.findByUsername(userDTO.getUsername());
        if (existingUser.isEmpty()) {
            logger.error("Invalid username or password: Username does not exist.");
            throw new IllegalArgumentException("Invalid username or password");
        }

        User user = existingUser.get();

        // Log encoded and raw passwords for debugging
        logger.debug("Encoded password from DB: {}", user.getPassword());
        logger.debug("Password to match: {}", userDTO.getPassword());

        // Check if the provided password matches the stored password
        if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            logger.error("Invalid username or password: Password does not match.");
            throw new IllegalArgumentException("Invalid username or password");
        }

        // Determine the role and log appropriate message
        String role = user.getRole(); // Assuming `getRole()` method exists and returns user role
        if ("ADMIN".equalsIgnoreCase(role)) {
            logger.info("Admin logged in: {}", userDTO.getUsername());
        } else if ("USER".equalsIgnoreCase(role)) {
            logger.info("User logged in: {}", userDTO.getUsername());
        } else {
            logger.warn("User with unknown role logged in: {}", userDTO.getUsername());
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
        logger.info("Fetching user by role: {}", role);

        // Find the user by role (assuming findByRole returns a User or Optional<User>)
        Optional<User> userOptional = userRepository.findByRole(role);

        // Check if the user exists, then convert to DTO
        return userOptional.map(UserConverter::convertToDTO);

    }

    @Override
    public List<UserDTO> getAllUser() {
        logger.info("Fetching all users");

        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            logger.warn("No users found in the system.");
            throw new ResourceNotFoundException("No users found.");
        }

        return users.stream()
                .filter(user -> user.getId() != 1) // Exclude user with ID 1
                .map(UserConverter::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Long id) {
        logger.info("Fetching product by ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with ID " + id + " not found."));

        return UserConverter.convertToDTO(user);
    }
}