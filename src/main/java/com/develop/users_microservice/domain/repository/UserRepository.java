package com.develop.users_microservice.domain.repository;

import com.develop.users_microservice.domain.model.Role;
import com.develop.users_microservice.domain.model.User;
import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;


public interface UserRepository {
    List<User> findAll();
    List<User> findByEnabled(boolean enabled);
    Optional<User> findById(Long id);
    User save(User user);
    void deleteById(Long id);
    List<User> findWithFilters(String name, String email, Boolean enabled, Long roleId);
    Optional<Role> findRoleById(Long roleId);
}
