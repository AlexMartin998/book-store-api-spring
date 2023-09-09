package com.alex.security.users.service;

import com.alex.security.auth.entity.Role;
import com.alex.security.auth.repository.RoleRepository;
import com.alex.security.common.constants.RoleConstants;
import com.alex.security.common.constants.RoleEnum;
import com.alex.security.common.exceptions.BadRequestException;
import com.alex.security.common.exceptions.ResourceNotFoundException;
import com.alex.security.users.entity.Usuario;
import com.alex.security.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;


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
        user.setRoles(Collections.singleton(role));  // Collections.singleton()  --> return  Set<T>

        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario findOneByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                // to customize ErrMsg in GlobalExceptionHandler <-- Auth
                () -> new UsernameNotFoundException("User not found with email: ".concat(email))
        );
    }

}
