package com.example.socialApp.role.web;

import com.example.socialApp.role.model_repo.ERole;
import com.example.socialApp.role.model_repo.Role;

import java.util.Set;

public interface RoleService {

    Role findByRole(ERole role);

    Set<Role> getRolesFromStringsSet(Set<String> strRoles);
}
