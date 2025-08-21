package com.contactslist.api.domain.repository;

import com.contactslist.api.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findByEmailOrUsername(String emailOrUsername);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    User save(User user);
    Optional<User> findById(UUID id);
}
