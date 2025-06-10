package com.develop.users_microservice.presentation.dto;

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
}
