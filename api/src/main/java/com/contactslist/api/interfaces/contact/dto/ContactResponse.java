package com.contactslist.api.interfaces.contact.dto;

import com.contactslist.api.domain.model.Relationship;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ContactResponse(
        UUID id, String firstName, String lastName, LocalDate birthDate,
        Relationship relationship, List<PhoneDTO> phones
) {
}
