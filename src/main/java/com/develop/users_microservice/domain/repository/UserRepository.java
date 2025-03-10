package com.develop.users_microservice.domain.repository;

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
    @Modifying
    @Query("UPDATE User u SET u.enabled = false WHERE u.id = :id")
    void deleteById(Long id);

    @Query("SELECT u FROM User u WHERE " +
            "(:name IS NULL OR u.name LIKE %:name%) AND " +
            "(:email IS NULL OR u.email = :email) AND " +
            "(:enabled IS NULL OR u.enabled = :enabled) AND " +
            "(:roleId IS NULL OR u.role.id = :roleId)")
    List<User> findWithFilters(@Param("name") String name,
                               @Param("email") String email,
                               @Param("enabled") Boolean enabled,
                               @Param("roleId") Long roleId);
}
