package com.contactslist.api.domain.repository;

import com.contactslist.api.domain.model.Contact;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;
import java.util.Optional;
import java.util.UUID;

public interface ContactRepository {
    Page<Contact> findAllByOwner(UUID ownerId, Pageable pageable);
    Optional<Contact> findByIdAndOwner(UUID id, UUID ownerId);
    Contact save(Contact c);
    void delete(Contact c);
}
