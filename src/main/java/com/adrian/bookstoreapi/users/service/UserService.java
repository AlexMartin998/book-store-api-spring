package com.adrian.bookstoreapi.users.service;

import com.adrian.bookstoreapi.users.dto.PaginatedUsersResponseDto;
import com.adrian.bookstoreapi.users.dto.UserRequestDto;
import com.adrian.bookstoreapi.users.dto.UserResponseDto;
import com.adrian.bookstoreapi.users.entity.Usuario;
import org.springframework.data.domain.Pageable;


public interface UserService {

    Usuario create(Usuario user);

    Usuario findOneByEmail(String email);

    PaginatedUsersResponseDto findAll(Pageable pageable);

    Usuario findOne(Long id);

    UserResponseDto update(Long id, UserRequestDto userRequestDto);
}
