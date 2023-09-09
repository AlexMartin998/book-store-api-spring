package com.alex.security.auth.service;

import com.alex.security.auth.dto.AuthResponseDto;
import com.alex.security.auth.dto.LoginRequestDto;
import com.alex.security.auth.dto.RegisterRequestDto;


public interface AuthService {

    AuthResponseDto register(RegisterRequestDto registerDto);

    AuthResponseDto login(LoginRequestDto loginDto);

    AuthResponseDto renewJwt(String userEmail);

}
