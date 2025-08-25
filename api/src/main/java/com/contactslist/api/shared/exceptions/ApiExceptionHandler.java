package com.contactslist.api.shared.exceptions;

import com.contactslist.api.interfaces.contact.dto.ApiMessage;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidation(MethodArgumentNotValidException ex){
        var errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, DefaultMessageSourceResolvable::getDefaultMessage, (a, b)->a));
        return Map.of("success", false, "message", "Dados inválidos", "errors", errors);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiMessage notFound(RuntimeException ex){ return new ApiMessage(false, ex.getMessage()); }

    @ExceptionHandler({UnauthorizedException.class, AccessDeniedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiMessage unauthorized(RuntimeException ex){ return new ApiMessage(false, ex.getMessage()); }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiMessage business(RuntimeException ex){ return new ApiMessage(false, ex.getMessage()); }

}
