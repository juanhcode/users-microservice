package com.develop.users_microservice.infrastructure.repository;
import com.develop.users_microservice.domain.model.Role;
import com.develop.users_microservice.domain.model.User;
import com.develop.users_microservice.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository{

    private final JpaUserRepository jpaUserRepository;

    @Override
    public List<User> findAll() {
        return jpaUserRepository.findAll();
    }

    @Override
    public List<User> findByEnabled(boolean enabled) {
        return jpaUserRepository.findByEnabled(enabled);
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaUserRepository.findById(id);
    }

    @Override
    public User save(User user) {
        return jpaUserRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        jpaUserRepository.deleteById(id);
    }

    @Override
    public List<User> findWithFilters(String name, String email, Boolean enabled, Long roleId) {
        return jpaUserRepository.findWithFilters(name, email, enabled, roleId);
    }

    @Override
    public Optional<Role> findRoleById(Long roleId) {
        return jpaUserRepository.findRoleById(roleId);
    }
}
