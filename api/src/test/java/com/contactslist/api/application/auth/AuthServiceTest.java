package com.contactslist.api.application.auth;

import com.contactslist.api.domain.model.User;
import com.contactslist.api.domain.repository.UserRepositoryPort;
import com.contactslist.api.infrastructure.security.JwtService;
import com.contactslist.api.interfaces.auth.dto.LoginRequest;
import com.contactslist.api.interfaces.auth.dto.RegisterRequest;
import com.contactslist.api.interfaces.auth.dto.AuthResponse;
import com.contactslist.api.shared.exceptions.BusinessException;
import com.contactslist.api.shared.exceptions.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepositoryPort users;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private JwtService jwt;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_shouldThrowException_whenEmailExists() {
        RegisterRequest request = new RegisterRequest("user", "teste@email.com", "123456");
        when(users.existsByEmail(request.email())).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> authService.register(request));
        assertEquals("E-mail já cadastrado", exception.getMessage());
    }

    @Test
    void register_shouldThrowException_whenUsernameExists() {
        RegisterRequest request = new RegisterRequest("user", "teste@email.com", "123456");
        when(users.existsByUsername(request.username())).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> authService.register(request));
        assertEquals("Username já cadastrado", exception.getMessage());
    }

    @Test
    void register_shouldSaveUser_whenDataIsValid() {
        RegisterRequest request = new RegisterRequest("user", "teste@email.com", "123456");
        when(users.existsByEmail(request.email())).thenReturn(false);
        when(users.existsByUsername(request.username())).thenReturn(false);
        when(encoder.encode(request.password())).thenReturn("encodedPassword");

        authService.register(request);

        verify(users, times(1)).save(any(User.class));
    }

    @Test
    void login_shouldThrowUnauthorized_whenUserNotFound() {
        LoginRequest request = new LoginRequest("user", "123456");
        when(users.findByEmailOrUsername(request.usernameOrEmail())).thenReturn(Optional.empty());

        UnauthorizedException exception = assertThrows(UnauthorizedException.class,
                () -> authService.login(request));
        assertEquals("Credenciais inválidas", exception.getMessage());
    }

    @Test
    void login_shouldReturnToken_whenCredentialsValid() {
        LoginRequest request = new LoginRequest("user", "123456");
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setPasswordHash("encodedPassword");

        when(users.findByEmailOrUsername(request.usernameOrEmail())).thenReturn(Optional.of(user));
        when(encoder.matches(request.password(), user.getPasswordHash())).thenReturn(true);
        when(jwt.generateToken(user.getId().toString())).thenReturn("token123");

        AuthResponse response = authService.login(request);

        assertEquals("token123", response.token());
    }
}
