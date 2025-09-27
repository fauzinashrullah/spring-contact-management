package com.learn.spring.restful.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learn.spring.restful.entity.User;

public interface UserRepository extends JpaRepository<User, String>{
    
}
