package com.alex.security.auth.service;

import com.alex.security.auth.entity.Role;
import com.alex.security.auth.repository.RoleRepository;
import com.alex.security.common.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;


    @Override
    public Role findOneByName(String name) {
        return roleRepository.findByName(name).orElseThrow(
                () -> new ResourceNotFoundException("Role", "name", name)
        );
    }

}
