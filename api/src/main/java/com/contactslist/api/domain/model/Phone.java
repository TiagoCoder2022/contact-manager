package com.contactslist.api.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Table(name = "phones")
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
public class Phone {
    @Id private UUID id = UUID.randomUUID();

    @ManyToOne(optional=false, fetch= FetchType.LAZY)
    @JoinColumn(name="contact_id") private Contact contact;

    @Column(nullable=false) private String number;
    private String label;
}
