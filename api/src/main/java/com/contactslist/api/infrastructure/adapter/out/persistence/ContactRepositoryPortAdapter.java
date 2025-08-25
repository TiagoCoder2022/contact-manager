package com.contactslist.api.infrastructure.adapter.out.persistence;

import com.contactslist.api.domain.model.Contact;
import com.contactslist.api.domain.repository.ContactRepositoryPort;


import com.contactslist.api.infrastructure.adapter.out.persistence.jpa.ContactJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ContactRepositoryPortAdapter implements ContactRepositoryPort {
    private final ContactJpaRepository repo;

    public Page<Contact> findAllByOwner(UUID ownerId, Pageable pageable){ return repo.findAllByOwner_Id(ownerId, pageable); }
    public Optional<Contact> findByIdAndOwner(UUID id, UUID ownerId){ return repo.findByIdAndOwner_Id(id, ownerId); }
    public Contact save(Contact contact){ return repo.save(contact); }
    public void delete(Contact contact){ repo.delete(contact); }
}
