package dev.vivek.authenticationservice.repositories;

import dev.vivek.authenticationservice.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findAllByIdIn(List<Long> roleIds);

    Optional<Role> findById(Long id);
}
