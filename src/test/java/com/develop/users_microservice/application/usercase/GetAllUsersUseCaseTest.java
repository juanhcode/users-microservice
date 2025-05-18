package com.develop.users_microservice.application.usercase;

import com.develop.users_microservice.presentation.dto.UserRequestDTO;
import com.develop.users_microservice.presentation.dto.UserResponseDTO;
import com.develop.users_microservice.application.usecase.GetAllUsersUseCase;
import com.develop.users_microservice.domain.model.Role;
import com.develop.users_microservice.domain.model.User;
import com.develop.users_microservice.domain.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetAllUsersUseCaseTest {
    @Mock
    private UserService userRepository;

    @InjectMocks
    private GetAllUsersUseCase getAllUsersUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks antes de cada test
    }

    @Test
    void shouldReturnAllEnabledUsersWhenFilterIsNull() {
        // Arrange
        List<User> mockUsers = new ArrayList<>();
        User user = new User();
        user.setId(1L);
        user.setName("John");
        user.setLastName("Doe");
        user.setEmail("john@example.com");
        user.setEnabled(true);
        mockUsers.add(user);

        when(userRepository.findByEnabled(true)).thenReturn(mockUsers);

        // Act
        List<User> result = getAllUsersUseCase.execute(null);

        // Assert
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getName());
        verify(userRepository, times(1)).findByEnabled(true);
    }

    @Test
    void shouldSaveUserSuccessfully() {
        // Arrange
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setName("Jane");
        userRequestDTO.setLastName("Doe");
        userRequestDTO.setEmail("jane@example.com");
        userRequestDTO.setAddress("123 Street");
        userRequestDTO.setEnabled(true);
        userRequestDTO.setRoleId(1L);

        Role role = new Role(1L, "Admin");

        when(userRepository.findRoleById(1L)).thenReturn(Optional.of(role));

        User userToSave = new User();
        userToSave.setName("Jane");
        userToSave.setLastName("Doe");
        userToSave.setEmail("jane@example.com");
        userToSave.setAddress("123 Street");
        userToSave.setEnabled(true);
        userToSave.setRole(role);

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("Jane");
        savedUser.setLastName("Doe");
        savedUser.setEmail("jane@example.com");
        savedUser.setAddress("123 Street");
        savedUser.setEnabled(true);
        savedUser.setRole(role);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        UserResponseDTO response = getAllUsersUseCase.save(userRequestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Jane", response.getName());
        assertEquals("Admin", response.getRole().getName());
        verify(userRepository, times(1)).findRoleById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowWhenRoleNotFound() {
        // Arrange
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setName("Jane");
        userRequestDTO.setLastName("Doe");
        userRequestDTO.setEmail("jane@example.com");
        userRequestDTO.setAddress("123 Street");
        userRequestDTO.setEnabled(true);
        userRequestDTO.setRoleId(99L); // Role que no existe

        when(userRepository.findRoleById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            getAllUsersUseCase.save(userRequestDTO);
        });

        assertEquals("No se encontró el rol con ID: 99", exception.getMessage());
        verify(userRepository, times(1)).findRoleById(99L);
    }
}
