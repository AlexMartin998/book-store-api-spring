package com.alex.security.auth.service;

import com.alex.security.auth.dto.AuthResponseDto;
import com.alex.security.auth.dto.LoginRequestDto;
import com.alex.security.auth.dto.RegisterRequestDto;
import com.alex.security.users.entity.Usuario;
import com.alex.security.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final CustomUserDetailsService customUserDetailsService;

    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    @Override
    public AuthResponseDto register(RegisterRequestDto registerDto) {
        Usuario newUser = createUserFromDto(registerDto);
        Usuario createdUser = userService.create(newUser);
        return generateAuthResponse(createdUser);
    }

    @Override
    public AuthResponseDto login(LoginRequestDto loginDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );

        Usuario user = userService.findOneByEmail(loginDto.getEmail());

        return generateAuthResponse(user);
    }

    @Override
    public AuthResponseDto renewJwt(String userEmail) {
        Usuario user = userService.findOneByEmail(userEmail);

        return generateAuthResponse(user);
    }


    private Usuario createUserFromDto(RegisterRequestDto registerDto) {
        Usuario newUser = modelMapper.map(registerDto, Usuario.class);
        newUser.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        return newUser;
    }

    private AuthResponseDto generateAuthResponse(Usuario user) {
        var userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());
        var jwtToken = jwtService.generateJwt(userDetails);
        var userDto = modelMapper.map(user, AuthResponseDto.UserDto.class);

        return AuthResponseDto.builder()
                .token(jwtToken)
                .user(userDto)
                .build();
    }

}
