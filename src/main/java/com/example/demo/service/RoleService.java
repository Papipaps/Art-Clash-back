package com.example.demo.service;

import com.example.demo.model.data.Role;

public interface RoleService {

    void addRoleToUser(String username,String roleName);

    Role saveRole(Role role);
}
