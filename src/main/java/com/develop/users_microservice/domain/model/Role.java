package com.develop.users_microservice.domain.model;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Cambiado a Long para ser compatible con autoincremento

    @Column(nullable = false, length = 50)
    private String name;
}
