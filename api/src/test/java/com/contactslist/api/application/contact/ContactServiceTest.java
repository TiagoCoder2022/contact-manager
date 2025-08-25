package com.contactslist.api.application.contact;

import com.contactslist.api.domain.model.Contact;
import com.contactslist.api.domain.model.Relationship;
import com.contactslist.api.domain.model.User;
import com.contactslist.api.domain.repository.ContactRepositoryPort;
import com.contactslist.api.domain.repository.UserRepositoryPort;
import com.contactslist.api.interfaces.contact.dto.ContactCreateRequest;
import com.contactslist.api.interfaces.contact.dto.ContactResponse;
import com.contactslist.api.interfaces.contact.dto.ContactUpdateRequest;
import com.contactslist.api.interfaces.contact.dto.PhoneDTO;
import com.contactslist.api.shared.exceptions.NotFoundException;
import com.contactslist.api.shared.exceptions.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ContactServiceTest {
    @Mock
    private ContactRepositoryPort contactRepositoryPort;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private ContactService contactService;

    private UUID userId;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userId = UUID.randomUUID();
        user = new User();
        user.setId(userId);
        user.setUsername("johndoe");

        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken(userId, null)
        );
    }

    @Test
     void create_shouldSaveContactAndReturnResponse() {
        ContactCreateRequest request = new ContactCreateRequest(
                "John", "Doe", LocalDate.of(1990, 1, 1), Relationship.AMIGO, List.of(new PhoneDTO("123456789", "casa"))
        );

        when(userRepositoryPort.findById(userId)).thenReturn(Optional.of(user));
        when(contactRepositoryPort.save(any(Contact.class))).thenAnswer(invocation -> {
            Contact contact = invocation.getArgument(0);
            contact.setId(UUID.randomUUID());
            return  contact;
        });

        ContactResponse response =  contactService.create(request);
    }

    @Test
    void list_shouldReturnContactsOfCurrentUser() {
        Contact contact = new Contact();
        contact.setId(UUID.randomUUID());
        contact.setOwner(user);
        contact.setFirstName("Alice");

        when(contactRepositoryPort.findAllByOwner(eq(userId), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(contact)));

        Page<ContactResponse> page = contactService.list(Pageable.unpaged());

        assertEquals(1, page.getTotalElements());
        assertEquals("Alice", page.getContent().get(0).firstName());
    }

    @Test
    void get_shouldReturnContact_whenFound() {
        UUID contactId = UUID.randomUUID();
        Contact contact = new Contact();
        contact.setId(contactId);
        contact.setOwner(user);
        contact.setFirstName("Bob");

        when(contactRepositoryPort.findByIdAndOwner(contactId, userId)).thenReturn(Optional.of(contact));

        ContactResponse response = contactService.get(contactId);

        assertEquals("Bob", response.firstName());
    }

    @Test
    void get_shouldThrowNotFound_whenContactNotExists() {
        UUID contactId = UUID.randomUUID();
        when(contactRepositoryPort.findByIdAndOwner(contactId, userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> contactService.get(contactId));
    }

    @Test
    void update_shouldModifyContactAndReturnResponse() {
        UUID contactId = UUID.randomUUID();
        Contact contact = new Contact();
        contact.setId(contactId);
        contact.setOwner(user);
        contact.setFirstName("Old");

        ContactUpdateRequest updateRequest = new ContactUpdateRequest(
                "NewName", "NewLast", LocalDate.of(2000, 1, 1), Relationship.AMIGO,
                List.of(new PhoneDTO("987654321", "mobile"))
        );

        when(contactRepositoryPort.findByIdAndOwner(contactId, userId)).thenReturn(Optional.of(contact));

        ContactResponse response = contactService.update(contactId, updateRequest);

        assertEquals("NewName", response.firstName());
        assertEquals(Relationship.AMIGO, response.relationship());
        assertEquals(1, response.phones().size());
    }

    @Test
    void delete_shouldRemoveContact() {
        UUID contactId = UUID.randomUUID();
        Contact contact = new Contact();
        contact.setId(contactId);
        contact.setOwner(user);

        when(contactRepositoryPort.findByIdAndOwner(contactId, userId)).thenReturn(Optional.of(contact));

        contactService.delete(contactId);

        verify(contactRepositoryPort, times(1)).delete(contact);
    }

    @Test
    void currentUserId_shouldThrowUnauthorized_whenNoAuth() {
        SecurityContextHolder.clearContext();
        assertThrows(UnauthorizedException.class, () -> contactService.list(Pageable.unpaged()));
    }
}
