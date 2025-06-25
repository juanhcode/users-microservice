package com.develop.users_microservice.application.usercase;

import com.develop.users_microservice.application.usecase.GetAllUsersUseCase;
import com.develop.users_microservice.domain.interfaces.UserService;
import com.develop.users_microservice.domain.model.Role;
import com.develop.users_microservice.domain.model.User;
import com.develop.users_microservice.presentation.dto.UserFilterRequest;
import com.develop.users_microservice.presentation.dto.UserRequestDTO;
import com.develop.users_microservice.presentation.dto.UserResponseDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllUsersUseCaseTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private GetAllUsersUseCase useCase;

    private User user;
    private Role role;

    @BeforeEach
    void setup() {
        role = new Role();
        role.setId(1L);
        role.setName("ADMIN");

        user = new User();
        user.setId(1L);
        user.setName("Juan");
        user.setLastName("Pérez");
        user.setEmail("juan@example.com");
        user.setAddress("Calle Falsa 123");
        user.setEnabled(true);
        user.setRole(role);
    }

    @Test
    void testExecuteWithoutFilter_ReturnsEnabledUsers() {
        when(userService.findByEnabled(true)).thenReturn(List.of(user));

        List<User> result = useCase.execute(null);

        assertEquals(1, result.size());
        verify(userService).findByEnabled(true);
    }

    @Test
    void testExecuteWithFilter_ReturnsFilteredUsers() {
        UserFilterRequest filter = new UserFilterRequest();
        filter.setName("Juan");
        filter.setEmail("juan@example.com");
        filter.setEnabled(true);
        filter.setRoleId(1L);

        when(userService.findWithFilters("Juan", "juan@example.com", true, 1L)).thenReturn(List.of(user));

        List<User> result = useCase.execute(filter);

        assertEquals(1, result.size());
        verify(userService).findWithFilters("Juan", "juan@example.com", true, 1L);
    }

    @Test
    void testSave_Success() {
        UserRequestDTO request = new UserRequestDTO();
        request.setName("Juan");
        request.setLastName("Pérez");
        request.setEmail("juan@example.com");
        request.setAddress("Calle Falsa");
        request.setEnabled(true);
        request.setRoleId(1L);

        when(userService.findRoleById(1L)).thenReturn(Optional.of(role));
        when(userService.save(any(User.class))).thenReturn(user);

        UserResponseDTO result = useCase.save(request);

        assertEquals("Juan", result.getName());
        assertEquals("ADMIN", result.getRole().getName());
        verify(userService).save(any(User.class));
    }

    @Test
    void testSave_RoleIdNull_ThrowsException() {
        UserRequestDTO request = new UserRequestDTO();
        request.setRoleId(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> useCase.save(request));
        assertEquals("El ID del rol no puede ser nulo", ex.getMessage());
    }

    @Test
    void testSave_RoleNotFound_ThrowsException() {
        UserRequestDTO request = new UserRequestDTO();
        request.setRoleId(99L);

        when(userService.findRoleById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> useCase.save(request));
        assertEquals("No se encontró el rol con ID: 99", ex.getMessage());
    }

    @Test
    void testUpdateUser_UserExists_Success() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setName("Carlos");
        dto.setEmail("nuevo@example.com");

        when(userService.findById(1L)).thenReturn(Optional.of(user));
        when(userService.findByEmail("nuevo@example.com")).thenReturn(Optional.empty());
        when(userService.save(any())).thenReturn(user);

        Optional<User> result = useCase.updateUser(1L, dto);

        assertTrue(result.isPresent());
        assertEquals("Carlos", result.get().getName());
        verify(userService).save(any(User.class));
    }

    @Test
    void testUpdateUser_UserNotFound_ReturnsEmpty() {
        UserRequestDTO dto = new UserRequestDTO();
        when(userService.findById(1L)).thenReturn(Optional.empty());

        Optional<User> result = useCase.updateUser(1L, dto);
        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateUser_EmailAlreadyUsed_ThrowsException() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setEmail("ya@usado.com");

        when(userService.findById(1L)).thenReturn(Optional.of(user));
        when(userService.findByEmail("ya@usado.com")).thenReturn(Optional.of(new User()));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> useCase.updateUser(1L, dto));
        assertEquals("El correo electrónico ya está en uso", ex.getMessage());
    }

    @Test
    void testGetUser_ReturnsUser() {
        when(userService.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = useCase.getUser(1L);

        assertTrue(result.isPresent());
        assertEquals("Juan", result.get().getName());
    }

    @Test
    void testDeleteUser_CallsRepository() {
        useCase.deleteUser(1L);
        verify(userService).deleteById(1L);
    }
}
