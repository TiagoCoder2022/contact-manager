package com.contactslist.api.application.auth;

import com.contactslist.api.interfaces.contact.dto.ApiMessage;
import com.contactslist.api.interfaces.dto.AuthResponse;
import com.contactslist.api.interfaces.dto.LoginRequest;
import com.contactslist.api.interfaces.dto.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiMessage> register(@Valid @RequestBody RegisterRequest req){
        service.register(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiMessage(true, "Cadastro realizado com sucesso"));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req){
        return ResponseEntity.ok(service.login(req));
    }
}
