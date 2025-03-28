package com.develop.users_microservice.infrastructure.event;


import com.develop.users_microservice.application.dto.UserRequestDTO;
import com.develop.users_microservice.application.usecase.GetAllUsersUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEventListener {
    private final GetAllUsersUseCase getAllUsersUseCase;
    private final ObjectMapper objectMapper;

    public void procesarEvento(String mensaje) {
        try {
            if (mensaje.startsWith("GET_USERS_EVENT")) {
                getAllUsersUseCase.execute(null);
            } else if (mensaje.startsWith("GET_USER_EVENT:")) {
                Long id = Long.parseLong(mensaje.split(":")[1]);
                getAllUsersUseCase.getUser(id);
            } else if (mensaje.startsWith("CREATE_USER_EVENT:")) {
                String jsonUser = mensaje.substring("CREATE_USER_EVENT:".length());
                UserRequestDTO userRequest = objectMapper.readValue(jsonUser, UserRequestDTO.class);
                getAllUsersUseCase.save(userRequest);
            } else if (mensaje.startsWith("DELETE_USER_EVENT:")) {
                Long id = Long.parseLong(mensaje.split(":")[1]);
                getAllUsersUseCase.deleteUser(id);
            } else {
                throw new IllegalArgumentException("Evento desconocido: " + mensaje);
            }
        } catch (Exception e) {
            System.err.println("Error procesando el evento: " + e.getMessage());
        }
    }

}
