package com.example.licenta.service;

import com.example.licenta.dto.ChangeRolesDTO;
import com.example.licenta.dto.CreateUserDTO;
import com.example.licenta.entities.Role;
import com.example.licenta.entities.User;
import com.example.licenta.exception.ResourceNotFoundException;
import com.example.licenta.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleService roleService;
    @Override
    public boolean createUser(CreateUserDTO createUserDTO){
        try {
            User createUser = new User();
            Role userRole = roleService.findRole("User");
            System.out.println(userRole + " " + userRole.getName());
            createUser.setEmail(createUserDTO.getEmail());
            createUser.setPassword(createUserDTO.getPassword());
            createUser.setFirstName(createUserDTO.getFirstName());
            createUser.setLastName(createUserDTO.getLastName());
            createUser.setAge(createUserDTO.getAge());
            createUser.setPhone(createUserDTO.getPhone());
            createUser.setNationality(createUserDTO.getNationality());
            createUser.setGender(createUserDTO.getGender());
            createUser.setRegistered(LocalDate.now());
            createUser.setConfirmed(false);
            createUser.setRoles(userRole);

            userRepository.save(createUser);

            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {

            String email = (String) authentication.getPrincipal();
            return userRepository.findByEmail(email);
        }

        return null;
    }

    @Override
    public void changeRoles(ChangeRolesDTO changeRolesDTO){
        Optional<User> user=userRepository.findByEmail(changeRolesDTO.getEmail());
        if(user.isEmpty()){
            throw new ResourceNotFoundException("");
        }
        Role role=roleService.findRole("Proprietar");
        User updateUser=user.get();
        updateUser.setRoles(role);
        System.out.println(updateUser.getRoles().getName());
        saveUser(updateUser);
    }
    @Override
    public void changeRoles(){
        Optional<User> user=getCurrentUser();
        Role role=roleService.findRole("Proprietar");
        User updateUser=user.get();
        updateUser.setRoles(role);
        System.out.println(updateUser.getRoles().getName());
        saveUser(updateUser);
    }
}
