package dev.vivek.authenticationservice.controllers;

import dev.vivek.authenticationservice.dtos.*;
import dev.vivek.authenticationservice.exceptions.UserAlreadyExistsException;
import dev.vivek.authenticationservice.exceptions.UserDoesNotExistException;
import dev.vivek.authenticationservice.models.SessionStatus;
import dev.vivek.authenticationservice.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto request) throws UserDoesNotExistException {
        return authService.login(request.getEmail(), request.getPassword());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto request) {
        return authService.logout(request.getToken(), request.getUserId());
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpRequestDto request) throws UserAlreadyExistsException {
        UserDto userDto = authService.signUp(request.getEmail(), request.getPassword());
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping("/validate")
    public ResponseEntity<ValidateTokenResponseDto> validateToken(@RequestBody ValidateTokenRequestDto request) {
        Optional<UserDto> userDtoOptional = authService.validate(request.getToken(), request.getUserId());
        if (userDtoOptional.isEmpty()) {
            ValidateTokenResponseDto response = new ValidateTokenResponseDto();
            response.setSessionStatus(SessionStatus.INVALID);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        ValidateTokenResponseDto response = new ValidateTokenResponseDto();
        response.setSessionStatus(SessionStatus.ACTIVE);
        response.setUserDto(userDtoOptional.get());
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
