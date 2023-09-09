package com.alex.security.users.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Data   // provee todos los getter/setter, toString, @RequiredArgsConstructor
@Entity
@Table(name = "_user")   // in postgresql and jpa/hibernate user is reserved
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // autoincrement
    private Long id;

    private String firstname;
    private String lastname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(columnDefinition = "boolean default false")
    private boolean deleted = false;

    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    @PrePersist
    private void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
