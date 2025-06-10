package com.develop.users_microservice.presentation.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String address;
    private boolean enabled;
    private RoleDTO role;

    @Getter
    @Setter
    public static class RoleDTO {
        private Long id;
        private String name;


    }


}
