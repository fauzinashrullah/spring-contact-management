package com.learn.spring.restful.controller;

import org.springframework.web.bind.annotation.RestController;

import com.learn.spring.restful.entity.User;
import com.learn.spring.restful.model.ContactResponse;
import com.learn.spring.restful.model.CreateContactRequest;
import com.learn.spring.restful.model.WebResponse;
import com.learn.spring.restful.service.ContactService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
public class ContactController {
    
    private final ContactService contactService;

    @PostMapping("/api/contacts")
    public WebResponse<ContactResponse> create(User user, @RequestBody CreateContactRequest request) {
        ContactResponse contactResponse = contactService.create(user, request);
        return WebResponse.<ContactResponse>builder().data(contactResponse).build();
    }
    
}
