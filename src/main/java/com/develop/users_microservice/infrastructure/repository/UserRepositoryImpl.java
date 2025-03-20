package com.develop.users_microservice.infrastructure.repository;
import com.develop.users_microservice.domain.model.User;
import com.develop.users_microservice.domain.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepositoryImpl  extends JpaRepository<User, Long>, UserRepository{
    //boolean findByEmail(String email);
    @Override
    List<User> findAll();

    @Override
    Optional<User> findById(Long id);

    @Override
    User save(User user);

    @Override
    void deleteById(Long id);

}
