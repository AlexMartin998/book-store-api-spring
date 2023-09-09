package com.alex.security.auth.jwt;

import com.alex.security.auth.service.CustomUserDetailsService;
import com.alex.security.auth.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component  // transform/register to a managed @Bean of Spring (Inject)
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    // construira constructor con cada property(FINAL) q le creemos a la clase y permitira la Inject en Auto
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtService jwtService;
    private final SecurityErrorResponse securityErrorResponse;


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,     // req como tal - aqui la interceptamos
            @NonNull HttpServletResponse response,   // res como tal
            @NonNull FilterChain filterChain         // continuara con la ejecucion de los demas filtros de la filterChain
    ) throws ServletException, IOException {
        final String jwt = getJwtFromRequest(request);

        if (StringUtils.hasText(jwt)) {
            try {
                String userEmail = jwtService.extractUsername(jwt);  // username 'cause jwt call like this (email, uuid, username)

                // // si ya esta auth NO debo actualizar el SecurityContextHolder ni demas cosas
                // si !== null significa q YA esta Auth
                if (StringUtils.hasText(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);

                    // validate if the JWT is valid
                    if (jwtService.isTokenValid(jwt, userDetails)) {
                        // this object is needed to UPDATE the SecurityContextHolder
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                        authenticationToken.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request)
                        );

                        // // Update SecurityContextHolder
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            } catch (Exception e) {
                // handling Filter exceptions that can't be caught by GlobalExceptionHandler
                securityErrorResponse.sendErrorResponse(
                        request,
                        response,
                        HttpServletResponse.SC_UNAUTHORIZED,
                        e.getMessage()
                );
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.replace("Bearer ", "");
        }

        return null;
    }

}
