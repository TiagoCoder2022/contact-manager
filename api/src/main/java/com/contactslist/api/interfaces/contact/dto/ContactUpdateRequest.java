package com.contactslist.api.interfaces.contact.dto;

import com.contactslist.api.domain.model.Relationship;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;
import java.util.List;

public record ContactUpdateRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotNull @Past LocalDate birthDate,
        Relationship relationship,
        @NotEmpty List<@Valid PhoneDTO> phones
) {
}
