package com.contactslist.api.infrastructure.persistence;

import com.contactslist.api.domain.model.User;
import com.contactslist.api.domain.repository.UserRepository;
import com.contactslist.api.infrastructure.persistence.jpa.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {
    private final UserJpaRepository repo;

    public Optional<User> findByEmailOrUsername(String v){
        return repo.findByEmailOrUsername(v, v);
    }
    public boolean existsByEmail(String e){ return repo.existsByEmail(e); }
    public boolean existsByUsername(String u){ return repo.existsByUsername(u); }
    public User save(User u){ return repo.save(u); }
    public Optional<User> findById(UUID id){ return repo.findById(id); }
}
