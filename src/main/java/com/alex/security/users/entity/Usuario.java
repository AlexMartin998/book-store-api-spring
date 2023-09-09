package com.alex.security.users.entity;

import com.alex.security.auth.entity.Role;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.Set;


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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles;


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
