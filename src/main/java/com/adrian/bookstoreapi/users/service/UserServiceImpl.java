package com.adrian.bookstoreapi.users.service;

import com.adrian.bookstoreapi.auth.entity.Role;
import com.adrian.bookstoreapi.auth.repository.RoleRepository;
import com.adrian.bookstoreapi.common.constants.RoleConstants;
import com.adrian.bookstoreapi.common.exceptions.BadRequestException;
import com.adrian.bookstoreapi.common.exceptions.ResourceNotFoundException;
import com.adrian.bookstoreapi.users.entity.Usuario;
import com.adrian.bookstoreapi.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    @Override
    @Transactional
    public Usuario create(Usuario user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BadRequestException("User Already Registered");
        }

        Role role = roleRepository.findByName(RoleConstants.USER).orElseThrow(
                () -> new ResourceNotFoundException("Role", "name", RoleConstants.USER)
        );
        user.setRole(role);  // Collections.singleton()  --> return  Set<T>

        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario findOneByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                // CustomUserDetailsService needs this exception
                () -> new UsernameNotFoundException("User not found with email: ".concat(email))
        );
    }

}