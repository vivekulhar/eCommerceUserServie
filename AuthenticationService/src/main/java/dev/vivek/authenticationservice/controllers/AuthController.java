package dev.vivek.authenticationservice.controllers;

import dev.vivek.authenticationservice.dtos.LoginDto;
import dev.vivek.authenticationservice.dtos.UserDto;
import dev.vivek.authenticationservice.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    AuthService authService;
    public AuthController(AuthService authService){
        this.authService = authService;
    }
    @PostMapping("/login")
    public String login(@RequestBody LoginDto loginRequestDto){
        return authService.login(loginRequestDto);
    }

    @GetMapping("/validate")
    public ResponseEntity<UserDto> validate(@RequestParam("token") String token){
        UserDto userDto = authService.validate(token);
        if(userDto == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}
