package dev.vivek.authenticationservice.services;

import dev.vivek.authenticationservice.dtos.LoginDto;
import dev.vivek.authenticationservice.dtos.UserDto;
import dev.vivek.authenticationservice.exceptions.UserAlreadyExistsException;
import dev.vivek.authenticationservice.exceptions.UserDoesNotExistException;
import dev.vivek.authenticationservice.models.Session;
import dev.vivek.authenticationservice.models.SessionStatus;
import dev.vivek.authenticationservice.models.User;
import dev.vivek.authenticationservice.repositories.SessionRepository;
import dev.vivek.authenticationservice.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMapAdapter;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private SessionRepository sessionRepository;


    public AuthService(UserRepository userRepository, SessionRepository sessionRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.passwordEncoder = passwordEncoder;
        //this.bCryptPasswordEncoder = passwordEncoder;
    }
    public ResponseEntity<UserDto> login(String email, String password) throws UserDoesNotExistException{
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new UserDoesNotExistException("User with email: " + email + " doesn't exist.");
        }

        User user = userOptional.get();
        /*if(!password.equals(user.getPassword())){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }*/
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
//        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
        //        RandomStringUtils randomStringUtils = new RandomStringUtils();
        // TODO: Update here to use Jwt
        // Payload:
        // {
        //    userId:
        //    email:
        //    roles: [
        //    ]
        // }
        // Map<String, Object> claimsMap;
        // claimsMap.add(userId, 123);
        Map<String, ?> claimsMap = Map.of(
                "email", user.getEmail(),
                "roles", user.getRoles(),
                "expiringAt", 234
        );
        SecretKey key = Jwts.SIG.HS256.key().build();
        String token = Jwts.builder()
                .header()
                .add("alg", "HS256")
                .add("typ","JWT")
                .and()
                .claims(claimsMap)
                .signWith(key)
                .compact();
//        String token = RandomStringUtils.randomAscii(20);
        MultiValueMapAdapter<String, String > headers = new MultiValueMapAdapter<>(new HashMap<>());
        headers.add("AUTH_TOKEN", token);

        Session session = new Session();
        session.setSessionStatus(SessionStatus.ACTIVE);
        session.setToken(token);
        session.setUser(user);
        sessionRepository.save(session);


        UserDto userDto = UserDto.from(user);
        ResponseEntity<UserDto> response = new ResponseEntity<>(
                userDto,
                headers,
                HttpStatus.OK
        );

        return response;
    }

    public ResponseEntity<Void> logout(String token, Long userId) {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);

        if (sessionOptional.isEmpty()) {
            return null;
        }

        Session session = sessionOptional.get();

        session.setSessionStatus(SessionStatus.LOGGED_OUT);

        sessionRepository.save(session);

        return ResponseEntity.ok().build();
    }

    public UserDto signUp(String email, String password) throws UserAlreadyExistsException {

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (!userOptional.isEmpty()) {
            throw new UserAlreadyExistsException("User with " + email + " already exists.");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        //user.setPassword(password);
        user.setRoles(new HashSet<>());
        User savedUser = userRepository.save(user);

        return UserDto.from(savedUser);
    }

    public Optional<UserDto> validate(String token, Long userId) {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);

        if (sessionOptional.isEmpty()) {
            return Optional.empty();
        }

        Session session = sessionOptional.get();

        if (!session.getSessionStatus().equals(SessionStatus.ACTIVE)) {
            return Optional.empty();
        }
        User user = userRepository.findById(userId).get();

//        if (!session.getExpiringAt() > new Date()) {
//            return SessionStatus.EXPIRED;
//        }

        return Optional.of(UserDto.from(user));
    }
}

/// GET /products/1 -> abcd1234abcd
//        /authentication/validate/abcd1234abcd>u_id=12 -> true
// if the token is any random strong, we will first need to make
// a db call to validate the token
// and then another call to get the details of the user
// auth-token%3AeyJjdHkiOiJ0ZXh0L3BsYWluIiwiYWxnIjoiSFMyNTYifQ.ewogICAiZW1haWwiOiAibmFtYW5Ac2NhbGVyLmNvbSIsCiAgICJyb2xlcyI6IFsKICAgICAgIm1lbnRvciIsCiAgICAgICJ0YSIKICAgXSwKICAgImV4cGlyYXRpb25EYXRlIjogIjIzcmRPY3RvYmVyMjAyMyIKfQ.r2FVQUCn6DNHir5AlEBT2XQMgO7aN4m3xg9zcuB-zxQ
// auth-token%3AeyJhbGciOiJIUzI1NiJ9.eyJjcmVhdGVkQXQiOjE2OTgwNzgzNDg0NTQsInJvbGVzIjpbXSwiZXhwaXJ5QXQiOjE5NjU2LCJlbWFpbCI6Im5hbWFuQHNjYWxlci5jb20ifQ._v1af8cc1YA-cEyHlX1BASwveBiASQeteWFM8UzWxfY
