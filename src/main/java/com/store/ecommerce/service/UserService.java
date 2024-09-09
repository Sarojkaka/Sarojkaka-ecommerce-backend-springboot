package com.store.ecommerce.service;

import com.store.ecommerce.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserDTO loginUser(UserDTO userDTO);

    UserDTO registerUser(UserDTO userDTO);

    Optional<UserDTO> findUserByRole(String admin);

    List<UserDTO> getAllUser();

    UserDTO getUserById(Long id);
}
