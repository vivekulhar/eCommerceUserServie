package dev.vivek.authenticationservice.services;

import dev.vivek.authenticationservice.dtos.UserDto;
import dev.vivek.authenticationservice.models.Role;
import dev.vivek.authenticationservice.models.User;
import dev.vivek.authenticationservice.repositories.RoleRepository;
import dev.vivek.authenticationservice.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public UserDto getUserDetails(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            return null;
        }

        return UserDto.from(userOptional.get());
    }

    public UserDto setUserRoles(Long userId, List<Long> roleIds) {
        Optional<User> userOptional = userRepository.findById(userId);
        List<Role> roles = roleRepository.findAllByIdIn(roleIds);
        if (roles == null || roles.isEmpty()) {
            return null;
        }
        if (userOptional.isEmpty()) {
            return null;
        }

        User user = userOptional.get();
        Set<Role> userRoles = new HashSet<>(user.getRoles());
        user.setRoles(userRoles);


        User savedUser = userRepository.save(user);

        return UserDto.from(savedUser);
    }
}
