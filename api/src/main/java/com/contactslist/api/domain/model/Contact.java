package com.contactslist.api.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.aspectj.asm.internal.Relationship;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "contacts")
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
public class Contact {
    @Id private UUID id = UUID.randomUUID();
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") private User owner;

    @Column(name="first_name", nullable=false) private String firstName;
    @Column(name="last_name", nullable=false) private String lastName;
    @Column(name="birth_date", nullable=false) private LocalDate birthDate;

    @Enumerated(EnumType.STRING) private Relationship relationship;

    @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Phone> phones = new ArrayList<>();
}
