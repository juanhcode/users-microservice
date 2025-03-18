package com.develop.users_microservice.application.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRequestDTO {
    private String name;
    private String lastName;
    private String email;
    private String address;
    private boolean enabled;
    private Long roleId;

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class RoleRequestDTO {
        private Long id;
    }
}
