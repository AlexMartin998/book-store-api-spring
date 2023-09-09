package com.alex.security.users.service;

import com.alex.security.auth.repository.RoleRepository;
import com.alex.security.users.entity.Usuario;
import com.alex.security.users.repository.UserRepository;
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
            throw new RuntimeException("Som error");
        }

        return null;
    }

    @Override
    public Usuario findOneByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                // to customize ErrMsg in GlobalExceptionHandler <-- Auth
                () -> new UsernameNotFoundException("User not found with email: ".concat(email))
        );
    }

}
