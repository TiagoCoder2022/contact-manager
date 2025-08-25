package com.contactslist.api.application.contact;

import com.contactslist.api.domain.model.Contact;
import com.contactslist.api.domain.model.Phone;
import com.contactslist.api.domain.model.User;
import com.contactslist.api.domain.repository.ContactRepositoryPort;
import com.contactslist.api.domain.repository.UserRepositoryPort;
import com.contactslist.api.interfaces.contact.dto.ContactCreateRequest;
import com.contactslist.api.interfaces.contact.dto.ContactResponse;
import com.contactslist.api.interfaces.contact.dto.ContactUpdateRequest;
import com.contactslist.api.interfaces.contact.dto.PhoneDTO;
import com.contactslist.api.shared.exceptions.NotFoundException;
import com.contactslist.api.shared.exceptions.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ContactService {
    private final ContactRepositoryPort contacts;
    private final UserRepositoryPort users;

    private UUID currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null)
            throw new UnauthorizedException("Token ausente ou inválido");
        return (UUID) auth.getPrincipal();
    }

    @Transactional
    public ContactResponse create(ContactCreateRequest request) {
        UUID currentUserId = currentUserId();
        User user = users.findById(currentUserId).orElseThrow();

        Contact contact = new Contact();
        contact.setOwner(user);
        contact.setFirstName(request.firstName());
        contact.setLastName(request.lastName());
        contact.setBirthDate(request.birthDate());
        contact.setRelationship(request.relationship());

        for (var phoneRequest : request.phones()) {
            Phone phone = new Phone();
            phone.setContact(contact);
            phone.setNumber(phoneRequest.number());
            phone.setLabel(phoneRequest.label());
            contact.getPhones().add(phone);
        }

        Contact savedContact = contacts.save(contact);
        return toResponse(savedContact);
    }

    public Page<ContactResponse> list(Pageable pageable){
        UUID currentUserId = currentUserId();
        return contacts.findAllByOwner(currentUserId, pageable).map(this::toResponse);
    }

    public ContactResponse get(UUID id){
        UUID currentUserId = currentUserId();
        var contact = contacts.findByIdAndOwner(id, currentUserId).orElseThrow(() -> new NotFoundException("Contato não encontrado"));
        return toResponse(contact);
    }

    @Transactional
    public ContactResponse update(UUID id, ContactUpdateRequest request) {
        UUID currentUserId = currentUserId();

        Contact contact = contacts.findByIdAndOwner(id, currentUserId)
                .orElseThrow(() -> new NotFoundException("Contato não encontrado"));

        contact.setFirstName(request.firstName());
        contact.setLastName(request.lastName());
        contact.setBirthDate(request.birthDate());
        contact.setRelationship(request.relationship());

        contact.getPhones().clear();

        for (var phoneRequest : request.phones()) {
            Phone newPhone = new Phone();
            newPhone.setContact(contact);
            newPhone.setNumber(phoneRequest.number());
            newPhone.setLabel(phoneRequest.label());

            contact.getPhones().add(newPhone);
        }

        return toResponse(contact);
    }


    @Transactional
    public void delete(UUID id){
        UUID currentUserId = currentUserId();
        var contact = contacts.findByIdAndOwner(id, currentUserId).orElseThrow(() -> new NotFoundException("Contato não encontrado"));
        contacts.delete(contact);
    }

    private ContactResponse toResponse(Contact contact){
        var phones = contact.getPhones().stream().map(p -> new PhoneDTO(p.getNumber(), p.getLabel())).toList();
        return new ContactResponse(contact.getId(), contact.getFirstName(), contact.getLastName(), contact.getBirthDate(), contact.getRelationship(), phones);
    }
}
