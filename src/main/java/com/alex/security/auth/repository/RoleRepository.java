package com.alex.security.auth.repository;

import com.alex.security.auth.entity.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface RoleRepository extends CrudRepository<Role, Long> {

    Optional<Role> findByName(String name);

}
