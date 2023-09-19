package com.adrian.bookstoreapi.auth.controller;

import com.adrian.bookstoreapi.auth.dto.AuthResponseDto;
import com.adrian.bookstoreapi.auth.dto.LoginRequestDto;
import com.adrian.bookstoreapi.auth.dto.RegisterRequestDto;
import com.adrian.bookstoreapi.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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

    @GetMapping("/roles")
    public ResponseEntity<List<AuthResponseDto.RoleDto>> findAllRoles() {
        return ResponseEntity.ok(authService.findAllRoles());
    }

}
