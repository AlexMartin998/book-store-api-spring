package com.adrian.bookstoreapi.users.repository;

import com.adrian.bookstoreapi.users.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


// JpaRepository x default me da el Page para el  .findAll()
public interface UserRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    Boolean existsByEmail(String email);

}

