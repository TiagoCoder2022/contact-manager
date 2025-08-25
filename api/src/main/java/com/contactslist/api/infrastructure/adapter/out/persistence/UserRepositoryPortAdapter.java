package com.contactslist.api.infrastructure.adapter.out.persistence;

import com.contactslist.api.domain.model.User;
import com.contactslist.api.domain.repository.UserRepositoryPort;
import com.contactslist.api.infrastructure.adapter.out.persistence.jpa.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserRepositoryPortAdapter implements UserRepositoryPort {
    private final UserJpaRepository repo;

    public Optional<User> findByEmailOrUsername(String emailOrUsername){
        return repo.findByEmailOrUsername(emailOrUsername, emailOrUsername);
    }
    public boolean existsByEmail(String email){ return repo.existsByEmail(email); }
    public boolean existsByUsername(String userName){ return repo.existsByUsername(userName); }
    public User save(User user){ return repo.save(user); }
    public Optional<User> findById(UUID id){ return repo.findById(id); }
}
