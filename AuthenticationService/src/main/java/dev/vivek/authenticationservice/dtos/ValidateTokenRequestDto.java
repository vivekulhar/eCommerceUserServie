package dev.vivek.authenticationservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateTokenRequestDto {
    // in reality you wont need userID but since random
    // function can produce two same tokens
    private Long userId;
    private String token;
}
