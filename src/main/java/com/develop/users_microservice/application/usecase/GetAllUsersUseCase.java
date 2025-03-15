package com.develop.users_microservice.application.usecase;

import com.develop.users_microservice.application.dto.UserFilterRequest;
import com.develop.users_microservice.domain.model.User;
import com.develop.users_microservice.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;


@Service
@RequiredArgsConstructor
public class GetAllUsersUseCase {
    private final UserRepository userRepository;

    public List<User> execute(UserFilterRequest filterRequest) {
        if (filterRequest == null) {
            return userRepository.findByEnabled(true);
        }
        return userRepository.findWithFilters(
                filterRequest.getName(),
                filterRequest.getEmail(),
                filterRequest.getEnabled(),
                filterRequest.getRoleId()
        );
    }

    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}