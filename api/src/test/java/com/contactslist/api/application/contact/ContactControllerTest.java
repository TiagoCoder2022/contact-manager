package com.contactslist.api.application.contact;


import com.contactslist.api.domain.model.Relationship;
import com.contactslist.api.infrastructure.adapter.in.web.ContactController;
import com.contactslist.api.infrastructure.security.JwtService;
import com.contactslist.api.interfaces.contact.dto.ContactCreateRequest;
import com.contactslist.api.interfaces.contact.dto.ContactResponse;
import com.contactslist.api.interfaces.contact.dto.ContactUpdateRequest;
import com.contactslist.api.interfaces.contact.dto.PhoneDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContactController.class)
@AutoConfigureMockMvc(addFilters = false)
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ContactService service;

    @MockitoBean
    private JwtService jwtService;

    private String fakeToken;

    @BeforeEach
    void setupMocks() {
        fakeToken = "fake-jwt-token";

        Mockito.when(jwtService.parseSubject(fakeToken))
                .thenReturn("test-user");
    }

    @Test
    void shouldCreateContact() throws Exception {
        var req = new ContactCreateRequest("João", "Silva", LocalDate.of(1990, 1, 1), Relationship.AMIGO, List.of(new PhoneDTO("123456789", "casa")));
        var resp = new ContactResponse(UUID.randomUUID(), "João", "Silva", LocalDate.of(1990, 1, 1), Relationship.AMIGO, List.of(new PhoneDTO("123456789", "casa")));

        Mockito.when(service.create(any(ContactCreateRequest.class))).thenReturn(resp);

        mockMvc.perform(post("/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Contato criado com sucesso"))
                .andExpect(jsonPath("$.data.firstName").value("João"));
    }

    @Test
    void shouldListContacts() throws Exception {
        var resp = new ContactResponse(UUID.randomUUID(), "Maria", "Oliveira", LocalDate.of(1990, 1, 1), null, null);
        var page = new PageImpl<>(List.of(resp), PageRequest.of(0, 10), 1);

        Mockito.when(service.list(any())).thenReturn(page);

        mockMvc.perform(get("/contacts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].firstName").value("Maria"));
    }

    @Test
    void shouldGetContactById() throws Exception {
        var id = UUID.randomUUID();
        var resp = new ContactResponse(id, "Pedro", "Santos", LocalDate.of(1990, 1, 1), null, null);

        Mockito.when(service.get(id)).thenReturn(resp);

        mockMvc.perform(get("/contacts/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Pedro"));
    }

    @Test
    void shouldUpdateContact() throws Exception {
        var id = UUID.randomUUID();
        var req = new ContactUpdateRequest("Pedro", "Silva", LocalDate.of(1990, 1, 1), Relationship.AMIGO, List.of(new PhoneDTO("123456789", "casa")));
        var resp = new ContactResponse(id, "Pedro", "Almeida", LocalDate.of(1990, 1, 1), Relationship.AMIGO, List.of(new PhoneDTO("123456789", "casa")));

        Mockito.when(service.update(eq(id), any(ContactUpdateRequest.class))).thenReturn(resp);

        mockMvc.perform(put("/contacts/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Contato atualizado com sucesso"))
                .andExpect(jsonPath("$.data.lastName").value("Almeida"));
    }

    @Test
    void shouldDeleteContact() throws Exception {
        var id = UUID.randomUUID();

        mockMvc.perform(delete("/contacts/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Contato deletado com sucesso"));

        Mockito.verify(service).delete(id);
    }
}
