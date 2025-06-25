package com.develop.users_microservice.application.usercase;

import com.develop.users_microservice.infrastructure.exception.GlobalExceptionHandler;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.springframework.http.HttpStatus.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @Mock
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        when(webRequest.getDescription(false)).thenReturn("uri=/test");
    }

    @Test
    void testHandleValidationExceptions_returnsBadRequest() {
        var target = new Object();
        var bindingResult = new BeanPropertyBindingResult(target, "user");
        bindingResult.addError(new FieldError("user", "email", "El correo es inválido"));

        var ex = new MethodArgumentNotValidException(null, bindingResult);

        var response = handler.handleValidationExceptions(ex, webRequest);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().get("errors").toString().contains("email"));
    }

    @Test
    void testHandleConstraintViolationException_returnsBadRequest() {
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);
        when(path.toString()).thenReturn("email");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("Debe ser válido");

        ConstraintViolationException ex = new ConstraintViolationException(Set.of(violation));

        var response = handler.handleConstraintViolationException(ex, webRequest);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().get("errors").toString().contains("email"));
        assertTrue(response.getBody().get("errors").toString().contains("Debe ser válido"));
    }


    @Test
    void testHandleRuntimeException_returnsBadRequest() {
        RuntimeException ex = new RuntimeException("Lógica inválida");

        var response = handler.handleRuntimeException(ex, webRequest);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Lógica inválida", response.getBody().get("message"));
    }

    @Test
    void testHandleDataIntegrityViolationException_duplicateEmail() {
        var cause = new Throwable("ERROR: duplicate key value violates unique constraint \"users_email_key\"");
        DataIntegrityViolationException ex = new DataIntegrityViolationException("violación", cause);

        var response = handler.handleDataIntegrityViolationException(ex, webRequest);

        assertEquals(CONFLICT, response.getStatusCode());
        assertEquals("El correo electrónico ya está en uso", response.getBody().get("message"));
    }

    @Test
    void testHandleDataIntegrityViolationException_genericError() {
        var cause = new Throwable("otro error inesperado");
        DataIntegrityViolationException ex = new DataIntegrityViolationException("fallo", cause);

        var response = handler.handleDataIntegrityViolationException(ex, webRequest);

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error interno del servidor", response.getBody().get("message"));
    }

    @Test
    void testHandleResponseStatusException_returnsCustomStatus() {
        ResponseStatusException ex = new ResponseStatusException(NOT_FOUND, "No encontrado");

        var response = handler.handleResponseStatusException(ex, webRequest);

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("No encontrado", response.getBody().get("message"));
    }
}

