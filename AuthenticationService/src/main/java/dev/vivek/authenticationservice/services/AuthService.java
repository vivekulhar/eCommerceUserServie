package dev.vivek.authenticationservice.services;

import dev.vivek.authenticationservice.dtos.LoginDto;
import dev.vivek.authenticationservice.dtos.UserDto;
import dev.vivek.authenticationservice.models.Session;
import dev.vivek.authenticationservice.models.User;
import dev.vivek.authenticationservice.repositories.SessionRepository;
import dev.vivek.authenticationservice.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;

@Service
public class AuthService {
    UserRepository userRepository;
    SessionRepository sessionRepository;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static SecureRandom random = new SecureRandom();

    public AuthService(UserRepository userRepository, SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }
    public String login(LoginDto loginRequestDto) {
        Optional<User> user = userRepository.findByEmail(loginRequestDto.getEmail());
        if(user.isPresent()){
            if(user.get().getPassword().equals(loginRequestDto.getPassword())){
                StringBuilder sb = new StringBuilder(20);
                for (int i = 0; i < 20; i++) {
                    sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
                }
                sessionRepository.save(new Session(sb.toString(), user.get()));
                return sb.toString();
            }
            return "Invalid Password";
        }
        return "User not found";
    }

    public UserDto validate(String token) {
        Optional<Session> session = sessionRepository.findByToken(token);
        if(session.isPresent()){
            User user = userRepository.findById(session.get().getUser().getId()).get();
            UserDto userDto = new UserDto();
            userDto.setEmail(user.getEmail());
            userDto.setFullName(user.getFullName());
            return userDto;
        }
        return null;
    }
}
