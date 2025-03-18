package com.develop.users_microservice.application.usecase;

import com.develop.users_microservice.application.dto.UserFilterRequest;
import com.develop.users_microservice.application.dto.UserRequestDTO;
import com.develop.users_microservice.application.dto.UserResponseDTO;
import com.develop.users_microservice.domain.model.User;
import com.develop.users_microservice.domain.model.Role;
import com.develop.users_microservice.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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

    // Aquí ya no necesitamos RoleRepository, usamos el UserRepository para obtener Role
    public UserResponseDTO save(UserRequestDTO userRequest) {
        // Validar que el role.id no sea nulo
        if (userRequest.getRoleId() == null) {
            throw new RuntimeException("El ID del rol no puede ser nulo");
        }
        // Buscar el rol completo por el ID recibido en el DTO del request
        Optional<Role> roleOpt = userRepository.findRoleById(userRequest.getRoleId());

        if (!roleOpt.isPresent()) {
            throw new RuntimeException("No se encontró el rol con ID: " + userRequest.getRoleId());
        }

        Role role = roleOpt.get();

        // Crear el usuario con los datos del DTO
        User user = new User();
        user.setName(userRequest.getName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setAddress(userRequest.getAddress());
        user.setEnabled(userRequest.isEnabled());
        user.setRole(role);  // Asignamos el Role obtenido de la base de datos

        // Guardar el usuario en la base de datos
        User savedUser = userRepository.save(user);

        // Convertir el usuario guardado a un DTO para la respuesta
        return toUserResponseDTO(savedUser);
    }

    // convertir un usuario a UserResponseDTO
    private UserResponseDTO toUserResponseDTO(User user) {
        UserResponseDTO userResponse = new UserResponseDTO();
        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setAddress(user.getAddress());
        userResponse.setEnabled(user.isEnabled());

        // Crear el DTO para el rol
        UserResponseDTO.RoleDTO roleDTO = new UserResponseDTO.RoleDTO();
        roleDTO.setId(user.getRole().getId());
        roleDTO.setName(user.getRole().getName());  // Obtenemos el nombre del rol

        userResponse.setRole(roleDTO);  // Asignamos el DTO del rol al DTO del usuario

        return userResponse;
    }

    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }
    //public User save(User user) {return userRepository.save(user);}
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}