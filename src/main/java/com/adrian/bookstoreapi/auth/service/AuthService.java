package com.adrian.bookstoreapi.auth.service;

import com.adrian.bookstoreapi.auth.dto.AuthResponseDto;
import com.adrian.bookstoreapi.auth.dto.LoginRequestDto;
import com.adrian.bookstoreapi.auth.dto.RegisterRequestDto;

import java.util.List;


public interface AuthService {

    AuthResponseDto register(RegisterRequestDto registerDto);

    AuthResponseDto login(LoginRequestDto loginDto);

    AuthResponseDto renewJwt(String userEmail);

     List<AuthResponseDto.RoleDto> findAllRoles();

}
