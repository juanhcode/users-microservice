package com.develop.users_microservice.infrastructure.service;
import com.develop.users_microservice.domain.model.Role;
import com.develop.users_microservice.domain.model.User;
import com.develop.users_microservice.domain.interfaces.UserService;
import com.develop.users_microservice.infrastructure.repository.JpaUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    public UserServiceImpl(
            JpaUserRepository jpaUserRepository
    ) {
        this.jpaUserRepository = jpaUserRepository;
    }

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
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email);
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
