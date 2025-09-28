package com.learn.spring.restful.controller;

import org.springframework.web.bind.annotation.RestController;

import com.learn.spring.restful.entity.User;
import com.learn.spring.restful.model.LoginUserRequest;
import com.learn.spring.restful.model.TokenResponse;
import com.learn.spring.restful.model.WebResponse;
import com.learn.spring.restful.service.AuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;

    @PostMapping("/api/auth/login")
    public WebResponse<TokenResponse> login(@RequestBody LoginUserRequest request) {
        TokenResponse tokenResponse = authService.login(request);
        return WebResponse.<TokenResponse>builder().data(tokenResponse).build();
    }

    @DeleteMapping("/api/auth/logout")
    public WebResponse<String> logout(User user){
        authService.logout(user);
        return WebResponse.<String>builder().data("OK").build();
    }
    
}
