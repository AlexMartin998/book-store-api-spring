package com.adrian.bookstoreapi.auth.repository;

import com.adrian.bookstoreapi.auth.entity.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface RoleRepository extends CrudRepository<Role, Long> {

    Optional<Role> findByName(String name);

}
