package com.learn.spring.restful.service;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.learn.spring.restful.entity.User;
import com.learn.spring.restful.model.LoginUserRequest;
import com.learn.spring.restful.model.TokenResponse;
import com.learn.spring.restful.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final ValidationService validationService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public TokenResponse login(LoginUserRequest request){
        validationService.validate(request);
        
        User user = userRepository.findById(request.getUsername())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password wrong"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password wrong");
        }

        user.setToken(UUID.randomUUID().toString());
        user.setTokenExpiredAt(next30Days());
        userRepository.save(user);

        return new TokenResponse(user.getToken(), user.getTokenExpiredAt());
    }

    public void logout(User user){
        user.setToken(null);
        user.setTokenExpiredAt(null);

        userRepository.save(user);
    }

    private Long next30Days(){
        return System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 30);
    }
}
