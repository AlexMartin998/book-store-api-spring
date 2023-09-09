package com.alex.security.auth.controller;

import com.alex.security.auth.dto.AuthResponseDto;
import com.alex.security.auth.dto.LoginRequestDto;
import com.alex.security.auth.dto.RegisterRequestDto;
import com.alex.security.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;

    @PostMapping("/register")
//    @Secured({RoleConstants.ADMIN, RoleConstants.USER}) // permite establecer varios roles
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequestDto request) {
        return new ResponseEntity<>(authService.register(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/renew-token")
    public ResponseEntity<AuthResponseDto> renewJwt(HttpServletRequest request, Authentication authentication) {
        String authUserEmail = ((UserDetails) authentication.getPrincipal()).getUsername();

        return ResponseEntity.ok(authService.renewJwt(authUserEmail));
    }

}
