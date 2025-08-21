package com.contactslist.api.infrastructure.persistence.jpa;

import com.contactslist.api.domain.model.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ContactJpaRepository extends JpaRepository<Contact, UUID> {
    Page<Contact> findAllByOwner_Id(UUID ownerId, Pageable pageable);
    Optional<Contact> findByIdAndOwner_Id(UUID id, UUID ownerId);
}
