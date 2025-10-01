package com.learn.spring.restful.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.learn.spring.restful.entity.Contact;
import com.learn.spring.restful.entity.User;

public interface ContactRepository extends JpaRepository<Contact, String>, JpaSpecificationExecutor<Contact>{
    
    Optional<Contact> findFirstByUserAndId(User user, String id);
}
