package com.contactslist.api.application.contact;

import com.contactslist.api.interfaces.contact.dto.ApiMessage;
import com.contactslist.api.interfaces.contact.dto.ContactCreateRequest;
import com.contactslist.api.interfaces.contact.dto.ContactResponse;
import com.contactslist.api.interfaces.contact.dto.ContactUpdateRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/contacts") @RequiredArgsConstructor
public class ContactController {
    private final ContactService service;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ContactCreateRequest req){
        var resp = service.create(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("success", true, "message", "Contato criado com sucesso", "data", resp));
    }

    @GetMapping
    public Page<ContactResponse> list(@PageableDefault(size=10, sort="firstName") Pageable pageable){
        return service.list(pageable);
    }

    @GetMapping("/{id}")
    public ContactResponse get(@PathVariable UUID id){ return service.get(id); }

    @PutMapping("/{id}")
    public Map<String, Object> update(@PathVariable UUID id, @Valid @RequestBody ContactUpdateRequest req){
        var resp = service.update(id, req);
        return Map.of("success", true, "message", "Contato atualizado com sucesso", "data", resp);
    }

    @DeleteMapping("/{id}")
    public ApiMessage delete(@PathVariable UUID id){
        service.delete(id);
        return new ApiMessage(true, "Contato deletado com sucesso");
    }
}
