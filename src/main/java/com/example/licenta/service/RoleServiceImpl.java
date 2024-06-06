package com.example.licenta.service;

import com.example.licenta.entities.Role;
import com.example.licenta.exception.ResourceNotFoundException;
import com.example.licenta.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService{
    @Autowired
    RoleRepository roleRepository;
    @Override
    public Role findRole(String name){
        return roleRepository.findByName(name);
    }

    @Override
    public Role findRole(int id){return roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(""));}
}
