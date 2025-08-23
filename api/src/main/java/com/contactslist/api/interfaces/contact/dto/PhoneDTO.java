package com.contactslist.api.interfaces.contact.dto;

import jakarta.validation.constraints.NotBlank;

public record PhoneDTO(
        @NotBlank String number,
        String label
) {
}
