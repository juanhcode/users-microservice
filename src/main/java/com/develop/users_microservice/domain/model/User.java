package com.develop.users_microservice.domain.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Ahora es manual

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role roleId;

    @Column(length = 255)
    private String address;

    @Column(nullable = false, length = 500)
    private String token; // Ahora se almacena en la BD
}
