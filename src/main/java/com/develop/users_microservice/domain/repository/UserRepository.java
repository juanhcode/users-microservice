package com.develop.users_microservice.domain.repository;

import com.develop.users_microservice.domain.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface UserRepository {
    List<User> findAll();
    Optional<User> findById(Long id);
    void deleteById(Long id);
}
