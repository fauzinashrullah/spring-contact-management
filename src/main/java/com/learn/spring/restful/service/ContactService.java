package com.learn.spring.restful.service;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.learn.spring.restful.entity.Contact;
import com.learn.spring.restful.entity.User;
import com.learn.spring.restful.model.ContactResponse;
import com.learn.spring.restful.model.CreateContactRequest;
import com.learn.spring.restful.repository.ContactRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContactService {
    
    private final ContactRepository contactRepository;
    private final ValidationService validationService;

    @Transactional
    public ContactResponse create(User user, CreateContactRequest request){
        validationService.validate(request);

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstname(request.getFirstname());
        contact.setLastname(request.getLastname());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contact.setUser(user);

        contactRepository.save(contact);

        return toContactResponse(contact);
    }

    public ContactResponse get(User user, String id){
        Contact contact = contactRepository.findFirstByUserAndId(user, id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));
        
        return toContactResponse(contact);
    }

    private ContactResponse toContactResponse(Contact contact){
        return new ContactResponse(
            contact.getId(), 
            contact.getFirstname(), 
            contact.getLastname(), 
            contact.getEmail(), 
            contact.getPhone()
        );
    }
}
