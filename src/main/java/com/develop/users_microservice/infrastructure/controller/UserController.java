package com.develop.users_microservice.infrastructure.controller;

import com.develop.users_microservice.application.usecase.GetAllUsersUseCase;
import com.develop.users_microservice.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final GetAllUsersUseCase getAllUsersUseCase;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(getAllUsersUseCase.execute());
    }
}
