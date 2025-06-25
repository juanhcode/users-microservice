package com.develop.users_microservice.application.usercase;

import com.develop.users_microservice.domain.model.Role;
import com.develop.users_microservice.domain.model.User;
import com.develop.users_microservice.infrastructure.repository.JpaUserRepository;
import com.develop.users_microservice.infrastructure.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private JpaUserRepository jpaUserRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
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
    }

    @Test
    void findAll_ReturnsList() {
        when(jpaUserRepository.findAll()).thenReturn(List.of(user));

        List<User> result = userService.findAll();

        assertEquals(1, result.size());
        assertEquals("Juan", result.get(0).getName());
    }

    @Test
    void findByEnabled_ReturnsEnabledUsers() {
        when(jpaUserRepository.findByEnabled(true)).thenReturn(List.of(user));

        List<User> result = userService.findByEnabled(true);

        assertEquals(1, result.size());
        assertTrue(result.get(0).isEnabled());
    }

    @Test
    void findById_ExistingUser_ReturnsOptional() {
        when(jpaUserRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("juan@mail.com", result.get().getEmail());
    }

    @Test
    void findById_NotFound_ReturnsEmpty() {
        when(jpaUserRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<User> result = userService.findById(2L);

        assertFalse(result.isPresent());
    }

    @Test
    void findByEmail_ReturnsUser() {
        when(jpaUserRepository.findByEmail("juan@mail.com")).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByEmail("juan@mail.com");

        assertTrue(result.isPresent());
        assertEquals("Juan", result.get().getName());
    }

    @Test
    void save_PersistsUser() {
        when(jpaUserRepository.save(user)).thenReturn(user);

        User result = userService.save(user);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(jpaUserRepository).save(user);
    }

    @Test
    void deleteById_CallsRepository() {
        userService.deleteById(1L);

        verify(jpaUserRepository).deleteById(1L);
    }

    @Test
    void findWithFilters_ReturnsFilteredUsers() {
        when(jpaUserRepository.findWithFilters("Juan", "juan@mail.com", true, 1L))
                .thenReturn(List.of(user));

        List<User> result = userService.findWithFilters("Juan", "juan@mail.com", true, 1L);

        assertEquals(1, result.size());
        assertEquals("Juan", result.get(0).getName());
    }

    @Test
    void findRoleById_ReturnsRole() {
        when(jpaUserRepository.findRoleById(1L)).thenReturn(Optional.of(role));

        Optional<Role> result = userService.findRoleById(1L);

        assertTrue(result.isPresent());
        assertEquals("Admin", result.get().getName());
    }
}
