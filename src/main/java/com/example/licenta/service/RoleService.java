package com.example.licenta.service;

import com.example.licenta.entities.Role;

public interface RoleService {
    Role findRole(String name);
    Role findRole(int id);
}
