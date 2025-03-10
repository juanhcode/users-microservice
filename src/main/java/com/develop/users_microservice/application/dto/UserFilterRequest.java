package com.develop.users_microservice.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserFilterRequest {
    private String name;
    private String email;
    private Boolean enabled = true;
    private Long roleId;
    private int page = 0; // Página por defecto
    private int size = 10; // Tamaño por defecto
}
