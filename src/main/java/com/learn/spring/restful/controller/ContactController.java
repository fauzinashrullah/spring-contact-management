package com.learn.spring.restful.controller;

import org.springframework.web.bind.annotation.RestController;

import com.learn.spring.restful.entity.User;
import com.learn.spring.restful.model.ContactResponse;
import com.learn.spring.restful.model.CreateContactRequest;
import com.learn.spring.restful.model.PagingResponse;
import com.learn.spring.restful.model.SearchContactRequest;
import com.learn.spring.restful.model.UpdateContactRequest;
import com.learn.spring.restful.model.WebResponse;
import com.learn.spring.restful.service.ContactService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequiredArgsConstructor
public class ContactController {
    
    private final ContactService contactService;

    @PostMapping("/api/contacts")
    public WebResponse<ContactResponse> create(User user, @RequestBody CreateContactRequest request) {
        ContactResponse contactResponse = contactService.create(user, request);
        return WebResponse.<ContactResponse>builder().data(contactResponse).build();
    }
    
    @GetMapping("/api/contacts/{contactId}")
    public WebResponse<ContactResponse> get(User user, @PathVariable("contactId") String contactId) {
        ContactResponse response = contactService.get(user, contactId);
        return WebResponse.<ContactResponse>builder().data(response).build();
    }
    
   @PutMapping("/api/contacts/{contactId}")
    public WebResponse<ContactResponse> update(User user, @RequestBody UpdateContactRequest request, @PathVariable String contactId) {
        ContactResponse contactResponse = contactService.update(user, request, contactId);
        return WebResponse.<ContactResponse>builder().data(contactResponse).build();
    }

    @DeleteMapping("/api/contacts/{contactId}")
    public WebResponse<String> delete(User user, @PathVariable("contactId") String contactId) {
        contactService.delete(user, contactId);
        return WebResponse.<String>builder().data("OK").build();
    }

    @GetMapping("/api/contacts")
    public WebResponse<List<ContactResponse>> search(   User user, 
                                                        @RequestParam(value = "name", required = false) String name, 
                                                        @RequestParam(value = "email", required = false) String email, 
                                                        @RequestParam(value = "phone", required = false) String phone, 
                                                        @RequestParam(value = "page", required = false, defaultValue = "0") Integer page, 
                                                        @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        SearchContactRequest request = SearchContactRequest.builder()
                                                            .page(page)
                                                            .size(size)
                                                            .name(name)
                                                            .email(email)
                                                            .phone(phone)
                                                            .build();

        Page<ContactResponse> contactResponses = contactService.search(user, request);
        return WebResponse.<List<ContactResponse>>builder()
            .data(contactResponses.getContent())
            .paging(PagingResponse.builder()
                .currentPage(contactResponses.getNumber())
                .totalPage(contactResponses.getTotalPages())
                .size(contactResponses.getSize())
                .build())
            .build();
    }
    
}