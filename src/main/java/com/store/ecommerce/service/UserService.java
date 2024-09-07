package com.store.ecommerce.service;

import com.store.ecommerce.dto.UserDTO;

import java.util.Optional;

public interface UserService {

    UserDTO loginUser(UserDTO userDTO);

    UserDTO registerUser(UserDTO userDTO);

    Optional<UserDTO> findUserByRole(String admin);
}
