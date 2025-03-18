package com.develop.users_microservice.infrastructure.controller;

import com.develop.users_microservice.application.dto.UserFilterRequest;
import com.develop.users_microservice.application.dto.UserRequestDTO;
import com.develop.users_microservice.application.dto.UserResponseDTO;
import com.develop.users_microservice.application.usecase.GetAllUsersUseCase;
import com.develop.users_microservice.domain.model.User;
import com.develop.users_microservice.domain.service.PasswordEncoder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.server.ResponseStatusException;

import javax.management.RuntimeErrorException;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final GetAllUsersUseCase getAllUsersUseCase;
    private final PasswordEncoder passwordEncoder;
    // Obtener todos los usuarios
    @PostMapping("/get-users")
    public ResponseEntity<List<User>> getAllUsers(@RequestBody(required = false) UserFilterRequest filterRequest) {
        List<User> users = getAllUsersUseCase.execute(filterRequest);
        return ResponseEntity.ok(users);
    }

    // Obtener un usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@Valid @PathVariable Long id) {
        return getAllUsersUseCase.getUser(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    // Crear un usuario
    @PostMapping
    public ResponseEntity<UserResponseDTO> createdUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO userResponseDTO = getAllUsersUseCase.save(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDTO); // retornaria un 201
    }

    // Eliminar un usuario por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        getAllUsersUseCase.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}