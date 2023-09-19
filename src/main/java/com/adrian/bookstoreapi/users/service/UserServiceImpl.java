package com.adrian.bookstoreapi.users.service;

import com.adrian.bookstoreapi.auth.entity.Role;
import com.adrian.bookstoreapi.auth.repository.RoleRepository;
import com.adrian.bookstoreapi.common.constants.RoleConstants;
import com.adrian.bookstoreapi.common.exceptions.BadRequestException;
import com.adrian.bookstoreapi.common.exceptions.ResourceNotFoundException;
import com.adrian.bookstoreapi.common.exceptions.UserNotFoundException;
import com.adrian.bookstoreapi.users.dto.PaginatedUsersResponseDto;
import com.adrian.bookstoreapi.users.dto.UserRequestDto;
import com.adrian.bookstoreapi.users.dto.UserResponseDto;
import com.adrian.bookstoreapi.users.entity.Usuario;
import com.adrian.bookstoreapi.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


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

    @Override
    public PaginatedUsersResponseDto findAll(Pageable pageable) {
        Page<Usuario> userPage = userRepository.findAll(pageable);
        List<UserResponseDto> userDtoList = userPage.getContent().stream()
                .map(user -> modelMapper.map(user, UserResponseDto.class))
                .toList();

        return PaginatedUsersResponseDto.builder()
                .users(userDtoList)
                .pageNumber(userPage.getNumber())
                .size(userPage.getSize())
                .totalElements(userPage.getTotalElements())
                .totalPages(userPage.getTotalPages())
                .isLastOne(userPage.isLast())
                .build();

    }

    @Override
    public Usuario findOne(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found with ID: ".concat(id.toString()))
        );
    }

    @Override
    public UserResponseDto update(Long id, UserRequestDto userRequestDto) {
        Usuario user = findOneById(id);
        Role role = roleRepository.findById(userRequestDto.getRoleId()).orElseThrow(
                () -> new ResourceNotFoundException("Role", "ID", userRequestDto.getRoleId())
        );

        Usuario userToSave = modelMapper.map(userRequestDto, Usuario.class);
        userToSave.setId(id);
        userToSave.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        userToSave.setRole(role);

        return modelMapper.map(userRepository.save(userToSave), UserResponseDto.class);
    }

    private Usuario findOneById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not fount with ID: ".concat(id.toString()))
        );
    }
}
