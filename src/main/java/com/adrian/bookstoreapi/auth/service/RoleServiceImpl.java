package com.adrian.bookstoreapi.auth.service;

import com.adrian.bookstoreapi.auth.entity.Role;
import com.adrian.bookstoreapi.auth.repository.RoleRepository;
import com.adrian.bookstoreapi.common.exceptions.ResourceNotFoundException;
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
