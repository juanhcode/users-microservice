package com.develop.users_microservice.infrastructure.controller;

import com.develop.users_microservice.application.dto.UserFilterRequest;
import com.develop.users_microservice.application.usecase.GetAllUsersUseCase;
import com.develop.users_microservice.domain.model.User;
import com.develop.users_microservice.domain.service.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return getAllUsersUseCase.getUser(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar un usuario por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        getAllUsersUseCase.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}