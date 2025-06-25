package com.develop.users_microservice.application.usercase;

import com.develop.users_microservice.application.usecase.GetAllUsersUseCase;
import com.develop.users_microservice.domain.model.Role;
import com.develop.users_microservice.domain.model.User;
import com.develop.users_microservice.presentation.controller.UserController;
import com.develop.users_microservice.presentation.dto.UserFilterRequest;
import com.develop.users_microservice.presentation.dto.UserRequestDTO;
import com.develop.users_microservice.presentation.dto.UserResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private GetAllUsersUseCase getAllUsersUseCase;

    @InjectMocks
    private UserController userController;

    private User user;
    private UserRequestDTO userRequestDTO;
    private UserResponseDTO userResponseDTO;
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1L);
        role.setName("Admin");

        user = new User();
        user.setId(1L);
        user.setName("Juan");
        user.setEmail("juan@mail.com");
        user.setEnabled(true);
        user.setRole(role);

        userRequestDTO = new UserRequestDTO();
        userRequestDTO.setName("Juan");
        userRequestDTO.setEmail("juan@mail.com");
        userRequestDTO.setEnabled(true);
        userRequestDTO.setRoleId(1L);

        userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(1L);
        userResponseDTO.setName("Juan");
        userResponseDTO.setEmail("juan@mail.com");
        userResponseDTO.setEnabled(true);
        UserResponseDTO.RoleDTO roleDTO = new UserResponseDTO.RoleDTO();
        roleDTO.setId(1L);
        roleDTO.setName("Admin");
        userResponseDTO.setRole(roleDTO);
    }

    @Test
    void getAllUsers_returnsUserList() {
        UserFilterRequest filterRequest = new UserFilterRequest();
        when(getAllUsersUseCase.execute(filterRequest)).thenReturn(List.of(user));

        ResponseEntity<List<User>> response = userController.getAllUsers(filterRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(user.getEmail(), response.getBody().get(0).getEmail());
    }

    @Test
    void getUser_existingId_returnsUser() {
        when(getAllUsersUseCase.getUser(1L)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getUser(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Juan", response.getBody().getName());
    }

    @Test
    void getUser_notFound_throwsException() {
        when(getAllUsersUseCase.getUser(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userController.getUser(1L);
        });

        assertEquals(404, exception.getStatusCode().value());
    }

    @Test
    void createdUser_returnsCreatedResponse() {
        when(getAllUsersUseCase.save(userRequestDTO)).thenReturn(userResponseDTO);

        ResponseEntity<UserResponseDTO> response = userController.createdUser(userRequestDTO);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Juan", response.getBody().getName());
    }

    @Test
    void updateUser_existingUser_returnsUpdatedUser() {
        when(getAllUsersUseCase.updateUser(eq(1L), any())).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.updateUser(1L, userRequestDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Juan", response.getBody().getName());
    }

    @Test
    void updateUser_notFound_throwsException() {
        when(getAllUsersUseCase.updateUser(eq(1L), any())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userController.updateUser(1L, userRequestDTO);
        });

        assertEquals(404, exception.getStatusCode().value());
    }

    @Test
    void deleteUser_deletesSuccessfully() {
        doNothing().when(getAllUsersUseCase).deleteUser(1L);

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(getAllUsersUseCase, times(1)).deleteUser(1L);
    }
}
