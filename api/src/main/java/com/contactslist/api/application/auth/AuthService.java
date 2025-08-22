package com.contactslist.api.application.auth;

import com.contactslist.api.domain.model.User;
import com.contactslist.api.domain.repository.UserRepository;
import com.contactslist.api.infrastructure.security.JwtService;
import com.contactslist.api.interfaces.dto.AuthResponse;
import com.contactslist.api.interfaces.dto.LoginRequest;
import com.contactslist.api.interfaces.dto.RegisterRequest;
import com.contactslist.api.shared.exceptions.BusinessException;
import com.contactslist.api.shared.exceptions.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    @Transactional
    public void register(RegisterRequest req) {
        if (users.existsByEmail(req.email())) throw new BusinessException("E-mail já cadastrado");
        if (users.existsByUsername(req.username())) throw new BusinessException("Username já cadastrado");

        var user = new User();

        user.setUsername(req.username());
        user.setEmail(req.email());
        user.setPasswordHash(encoder.encode(req.password()));
        users.save(user);
    }

    public AuthResponse login(LoginRequest req) {
        var u = users.findByEmailOrUsername(req.usernameOrEmail())
                .orElseThrow(() -> new UnauthorizedException("Credenciais inválidas"));

        if (!encoder.matches(req.password(), u.getPasswordHash())) {
            throw new UnauthorizedException("Credenciais inválidas");
        }

        return new AuthResponse(jwt.generateToken(u.getId().toString()));
    }
}
