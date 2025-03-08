package com.develop.users_microservice.domain.repository;

import com.develop.users_microservice.domain.model.User;

import java.util.List;

public interface UserRepository {
    List<User> findAll();
}
