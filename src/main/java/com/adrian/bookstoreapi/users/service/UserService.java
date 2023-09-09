package com.adrian.bookstoreapi.users.service;

import com.adrian.bookstoreapi.users.dto.PaginatedUsersResponseDto;
import com.adrian.bookstoreapi.users.entity.Usuario;
import org.springframework.data.domain.Pageable;


public interface UserService {

    Usuario create(Usuario user);

    Usuario findOneByEmail(String email);

    PaginatedUsersResponseDto findAll(Pageable pageable);

}
