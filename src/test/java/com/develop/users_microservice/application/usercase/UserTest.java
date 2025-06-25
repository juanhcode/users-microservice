package com.develop.users_microservice.application.usercase;


import com.develop.users_microservice.domain.model.Role;
import com.develop.users_microservice.domain.model.User;
import jakarta.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void noArgsConstructor_shouldCreateEmptyUser() {
        User user = new User();
        assertNotNull(user);
        assertNull(user.getId());
        assertNull(user.getName());
        assertFalse(user.isEnabled());
    }

    @Test
    void allArgsConstructor_shouldAssignAllFields() {
        Role role = new Role(1L, "ADMIN");
        User user = new User(
                1L,
                "Juan",
                "Pérez",
                "juan@example.com",
                role,
                "Calle Falsa 123",
                true
        );

        assertEquals(1L, user.getId());
        assertEquals("Juan", user.getName());
        assertEquals("Pérez", user.getLastName());
        assertEquals("juan@example.com", user.getEmail());
        assertEquals(role, user.getRole());
        assertEquals("Calle Falsa 123", user.getAddress());
        assertTrue(user.isEnabled());
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {
        User user = new User();
        Role role = new Role(2L, "USER");

        user.setId(10L);
        user.setName("Ana");
        user.setLastName("García");
        user.setEmail("ana@example.com");
        user.setRole(role);
        user.setAddress("Av. Siempre Viva");
        user.setEnabled(true);

        assertEquals(10L, user.getId());
        assertEquals("Ana", user.getName());
        assertEquals("García", user.getLastName());
        assertEquals("ana@example.com", user.getEmail());
        assertEquals(role, user.getRole());
        assertEquals("Av. Siempre Viva", user.getAddress());
        assertTrue(user.isEnabled());
    }

    @Test
    void toString_shouldIncludeKeyFields() {
        Role role = new Role(1L, "ADMIN");
        User user = new User(5L, "Laura", "Doe", "laura@example.com", role, null, true);

        String result = user.toString();
        assertTrue(result.contains("Laura"));
        assertTrue(result.contains("laura@example.com"));
        assertTrue(result.contains("true"));
    }

    @Test
    void validation_shouldFailIfNameIsBlank() {
        User user = new User();
        user.setName(" "); // Invalid
        user.setEmail("test@example.com");
        user.setRole(new Role(1L, "ADMIN"));
        user.setEnabled(true);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());

        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void validation_shouldFailIfEmailInvalid() {
        User user = new User();
        user.setName("Carlos");
        user.setEmail("invalid-email");
        user.setRole(new Role(1L, "ADMIN"));
        user.setEnabled(true);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());

        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void validation_shouldFailIfRoleIsNull() {
        User user = new User();
        user.setName("Carlos");
        user.setEmail("valid@example.com");
        user.setRole(null); // Invalid
        user.setEnabled(true);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());

        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("role")));
    }

    @Test
    void validation_shouldPassForValidUser() {
        User user = new User();
        user.setName("Lucía");
        user.setEmail("lucia@example.com");
        user.setRole(new Role(3L, "SELLER"));
        user.setEnabled(true);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }
}
