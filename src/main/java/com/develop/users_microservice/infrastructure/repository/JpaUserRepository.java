package com.develop.users_microservice.infrastructure.repository;

import com.develop.users_microservice.domain.model.Role;
import com.develop.users_microservice.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<User, Long> {
    List<User> findByEnabled(boolean enabled);

    @Modifying
    @Query("UPDATE User u SET u.enabled = false WHERE u.id = :id")
    void deleteById(@Param("id") Long id);

    @Query("SELECT u FROM User u WHERE " +
            "(:name IS NULL OR u.name LIKE %:name%) AND " +
            "(:email IS NULL OR u.email = :email) AND " +
            "(:enabled IS NULL OR u.enabled = :enabled) AND " +
            "(:roleId IS NULL OR u.role.id = :roleId)")
    List<User> findWithFilters(@Param("name") String name,
                               @Param("email") String email,
                               @Param("enabled") Boolean enabled,
                               @Param("roleId") Long roleId);

    @Query("SELECT r FROM Role r WHERE r.id = :roleId")
    Optional<Role> findRoleById(@Param("roleId") Long roleId);

    Optional<User> findByEmail(String email);
}
