package dev.vivek.authenticationservice.controllers;

import dev.vivek.authenticationservice.dtos.CreateRoleRequestDto;
import dev.vivek.authenticationservice.models.Role;
import dev.vivek.authenticationservice.services.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
public class RoleController {
    private RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/create")
    public ResponseEntity<Role> createRole(@RequestBody CreateRoleRequestDto request) {
        Role role = roleService.createRole(request.getName());
        return new ResponseEntity<>(role, HttpStatus.OK);
    }
    @GetMapping("/get")
    public ResponseEntity<Role> getRole(@RequestParam("id") Long id) {
        Role role = roleService.getRole(id);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }
}
