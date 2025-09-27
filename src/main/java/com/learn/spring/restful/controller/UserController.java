package com.learn.spring.restful.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learn.spring.restful.model.RegisterUserRequest;
import com.learn.spring.restful.model.WebResponse;
import com.learn.spring.restful.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;

    @PostMapping("/api/users")
    public WebResponse<String> register(@RequestBody RegisterUserRequest request) {
        userService.register(request);
        return WebResponse.<String>builder().data("OK").build();
    }
    
}
