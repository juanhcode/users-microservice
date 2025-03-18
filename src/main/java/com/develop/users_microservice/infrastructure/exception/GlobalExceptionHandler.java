package com.develop.users_microservice.infrastructure.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Manejo de errores para 400 - Bad Request (validaciones de campos)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (msg1, msg2) -> msg1));

        // Loguear el error
        logger.error("Error de validación con los campos: {}", fieldErrors);

        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Error de validación con los campos", fieldErrors, request);
    }

    // Manejo de errores para 400 - Bad Request (validaciones de JPA/Hibernate)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        Map<String, String> fieldErrors = ex.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        violation -> violation.getMessage()
                ));

        // Loguear el error
        logger.error("Error de validación con las restricciones: {}", fieldErrors);

        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Error de validación con los campos", fieldErrors, request);
    }

    // Manejo de errores para 400 - Bad Request (excepciones de lógica de negocio)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex, WebRequest request) {
        logger.error("Error en la lógica de negocio: {}", ex.getMessage(), ex);

        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), null, request);
    }

    // Manejo de errores de violación de restricciones únicas (correos duplicados)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        String errorMessage = ex.getMostSpecificCause().getMessage();

        if (errorMessage != null && errorMessage.contains("users_email_key")) {
            logger.error("Violación de restricción única (correo duplicado): {}", errorMessage);
            return buildErrorResponse(HttpStatus.CONFLICT, "El correo electrónico ya está en uso", null, request);
        }
        logger.error("Error interno del servidor: {}", errorMessage, ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", null, request);
    }
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex, WebRequest request) {
        String message = ex.getReason(); // Extrae solo el mensaje (sin el código de estado)
        logger.error("Respuesta de error con estado: {} y mensaje: {}", ex.getStatusCode(), message);
        return buildErrorResponse((HttpStatus) ex.getStatusCode(), message, null, request);
    }

    // Función para construir las respuestas de error
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