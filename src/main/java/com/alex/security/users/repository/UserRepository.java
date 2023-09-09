package com.alex.security.users.repository;

import com.alex.security.users.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


// JpaRepository x default me da el Page para el  .findAll()
public interface UserRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    Boolean existsByEmail(String email);

}

