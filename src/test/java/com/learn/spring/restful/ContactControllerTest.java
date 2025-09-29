package com.learn.spring.restful;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.spring.restful.entity.Contact;
import com.learn.spring.restful.entity.User;
import com.learn.spring.restful.model.ContactResponse;
import com.learn.spring.restful.model.CreateContactRequest;
import com.learn.spring.restful.model.WebResponse;
import com.learn.spring.restful.repository.ContactRepository;
import com.learn.spring.restful.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
@AutoConfigureMockMvc
class ContactControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void settUp(){
        contactRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("test");
        user.setPassword(passwordEncoder.encode("password"));
        user.setName("Test");
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() + 1000000000L);
        userRepository.save(user);
    }

    @Test
    void createContactBadRequest() throws Exception{
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstname("");
        request.setEmail("wrong");

        mockMvc.perform(
            post("/api/contacts")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-API-TOKEN", "test")
                .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
            status().isBadRequest()
        ).andDo(result -> {
            WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){
            });
            assertNotNull(response.getErrors());
        }
        );
    }

    @Test
    void createContactSuccess() throws Exception{
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstname("firstExample");
        request.setLastname("lastExample");
        request.setEmail("example@mail.com");
        request.setPhone("081234567890");

        mockMvc.perform(
            post("/api/contacts")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-API-TOKEN", "test")
                .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
            status().isOk()
        ).andDo(result -> {
            WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){
            });
            assertNull(response.getErrors());
            assertEquals("firstExample", response.getData().firstname());
            assertEquals("lastExample", response.getData().lastname());
            assertEquals("example@mail.com", response.getData().email());
            assertEquals("081234567890", response.getData().phone());

            assertTrue(contactRepository.existsById(response.getData().id()));
        }
        );
    }

    @Test
    void getContactNotFound() throws Exception{
        mockMvc.perform(
            get("/api/contacts/123456789")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-API-TOKEN", "test")
        ).andExpectAll(
            status().isNotFound()
        ).andDo(result -> {
            WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){
            });
            assertNotNull(response.getErrors());
        }
        );
    }

    @Test
    void getContactSuccess() throws Exception{
        User user = userRepository.findById("test").orElseThrow();

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setUser(user);
        contact.setFirstname("firstExample");
        contact.setLastname("lastExample");
        contact.setEmail("example@mail.com");
        contact.setPhone("081234567890");
        contactRepository.save(contact);

        mockMvc.perform(
            get("/api/contacts/" + contact.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-API-TOKEN", "test")
        ).andExpectAll(
            status().isOk()
        ).andDo(result -> {
            WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){
            });
            assertNull(response.getErrors());
            assertEquals(contact.getId(), response.getData().id());
            assertEquals(contact.getFirstname(), response.getData().firstname());
            assertEquals(contact.getLastname(), response.getData().lastname());
            assertEquals(contact.getEmail(), response.getData().email());
            assertEquals(contact.getPhone(), response.getData().phone());
        }
        );
    }
}
