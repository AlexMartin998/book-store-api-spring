package com.adrian.bookstoreapi.users.entity;

import com.adrian.bookstoreapi.auth.entity.Role;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;


@Data   // provee todos los getter/setter, toString, @RequiredArgsConstructor
@Entity
@Table(name = "_user")   // in postgresql and jpa/hibernate user is reserved
@Where(clause = "deleted = false") // filtra los deleted para todos los Select
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
    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    @JsonBackReference("role_ref")
    private Role role;


    // // for more control we can use those methods, but for simplicity `@EntityListeners(AuditingEntityListener.class)`
    @PrePersist
    private void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
