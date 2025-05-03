package com.develop.users_microservice.domain.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Ahora es manual

    @NotBlank(message = "El nombre no puede ser nulo")
    @Size(max = 50, message = "El nombre debe tener entre 1 y 50 caracteres")
    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String lastName;

    @NotBlank(message = "El correo electrónico es requerido")
    @Email(message = "El correo electrónico debe ser válido")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @ManyToOne
    @NotNull(message = "El rol es requerido")
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(length = 255)
    private String address;

    @Column(nullable = false)
    private boolean enabled;
}