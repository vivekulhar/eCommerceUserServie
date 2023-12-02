package dev.vivek.authenticationservice.services;

import dev.vivek.authenticationservice.models.Role;
import dev.vivek.authenticationservice.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {
    private RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role createRole(String name) {
        Role role = new Role();
        role.setName(name);

        return roleRepository.save(role);
    }

    public Role getRole(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        if (role.isEmpty()) {
            return null;
        }
        return role.get();
    }
}
