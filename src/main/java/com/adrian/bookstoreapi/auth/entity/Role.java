package com.adrian.bookstoreapi.auth.entity;

import com.adrian.bookstoreapi.users.entity.Usuario;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Set;


@Data
@Entity
//@Table(name = "role") // for singular it is not necessary
@Where(clause = "deleted = false")  // filtra los deleted para todos los Select
@EntityListeners(AuditingEntityListener.class) // Enable auditing features for createdAt,updatedAt
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "deleted")
    private boolean deleted = false;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "role")
    @JsonManagedReference("role_ref")
    private Set<Usuario> users;

}
