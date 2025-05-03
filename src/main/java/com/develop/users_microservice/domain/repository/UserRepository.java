package com.develop.users_microservice.domain.repository;

import com.develop.users_microservice.domain.model.Role;
import com.develop.users_microservice.domain.model.User;

import java.util.List;
import java.util.Optional;


public interface UserRepository {
    List<User> findAll();
    List<User> findByEnabled(boolean enabled);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    User save(User user);
    void deleteById(Long id);
    List<User> findWithFilters(String name, String email, Boolean enabled, Long roleId);
    Optional<Role> findRoleById(Long roleId);
}
