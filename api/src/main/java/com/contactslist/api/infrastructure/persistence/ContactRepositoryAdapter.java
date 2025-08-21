package com.contactslist.api.infrastructure.persistence;

import com.contactslist.api.domain.model.Contact;
import com.contactslist.api.domain.repository.ContactRepository;


import com.contactslist.api.infrastructure.persistence.jpa.ContactJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ContactRepositoryAdapter implements ContactRepository {
    private final ContactJpaRepository repo;

    public ContactRepositoryAdapter(ContactJpaRepository repo) {
        this.repo = repo;
    }

    public Page<Contact> findAllByOwner(UUID ownerId, Pageable p){ return repo.findAllByOwner_Id(ownerId, p); }
    public Optional<Contact> findByIdAndOwner(UUID id, UUID ownerId){ return repo.findByIdAndOwner_Id(id, ownerId); }
    public Contact save(Contact c){ return repo.save(c); }
    public void delete(Contact c){ repo.delete(c); }
}
