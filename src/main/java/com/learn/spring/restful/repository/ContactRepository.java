package com.learn.spring.restful.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learn.spring.restful.entity.Contact;

public interface ContactRepository extends JpaRepository<Contact, String>{
    
}
