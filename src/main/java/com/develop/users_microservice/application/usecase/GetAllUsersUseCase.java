package com.develop.users_microservice.application.usecase;

import com.develop.users_microservice.domain.model.User;
import com.develop.users_microservice.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;


@Service
@RequiredArgsConstructor
public class GetAllUsersUseCase {
    @Autowired
    private final UserRepository userRepository;

    public List<User> execute() {
        return userRepository.findAll();
    }

    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }

    public void deleteUser(Long id) {
        System.out.println("hola");
        userRepository.deleteById(id);
    }
}
