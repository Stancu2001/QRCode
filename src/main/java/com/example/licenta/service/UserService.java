package com.example.licenta.service;

import com.example.licenta.dto.ChangeRolesDTO;
import com.example.licenta.dto.CreateUserDTO;
import com.example.licenta.entities.User;

import java.util.Optional;

public interface UserService {
    boolean createUser(CreateUserDTO createUserDTO);

    Optional<User> findUserByEmail(String email);

    void saveUser(User user);

    Optional<User> getCurrentUser();
    void changeRoles(ChangeRolesDTO changeRolesDTO);
    void changeRoles();

}
