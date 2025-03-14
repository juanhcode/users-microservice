package com.develop.users_microservice.infrastructure.exception;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Manejo de errores para 400 - Bad Request
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        // Extraemos los errores de los campos
        Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (msg1, msg2) -> msg1));

        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Error de validación con los campos", fieldErrors, request);
    }

    // Manejo de errores generales como 404 - Not Found
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex, WebRequest request) {
        return buildErrorResponse((HttpStatus) ex.getStatusCode(), ex.getReason(), null, request);
    }

    // Manejo de errores como 500 - Internal Server Error (ya sea tambien por timeout)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", null, request);
    }

    // Funcion para construir las respuestas de error
    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String message, Map<String, String> fieldErrors, WebRequest request) {
        Map<String, Object> errors = new LinkedHashMap<>();
        errors.put("timestamp", LocalDateTime.now());
        errors.put("status", status.value());
        errors.put("error", status.getReasonPhrase());
        errors.put("message", message);
        errors.put("path", request.getDescription(false).replace("uri=", ""));

        if (fieldErrors != null && !fieldErrors.isEmpty()) {
            errors.put("errors", fieldErrors);
        }

        return new ResponseEntity<>(errors, status);
    }
}
