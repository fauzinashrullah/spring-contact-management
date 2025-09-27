package com.learn.spring.restful.model;

public record TokenResponse(String token, Long expiredAt) {
    
}
