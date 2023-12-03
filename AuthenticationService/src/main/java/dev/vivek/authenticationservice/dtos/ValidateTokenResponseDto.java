package dev.vivek.authenticationservice.dtos;

import dev.vivek.authenticationservice.models.SessionStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateTokenResponseDto {
    // in reality you wont need userID but since random
    // function can produce two same tokens
    private UserDto userDto;
    private SessionStatus sessionStatus;
}