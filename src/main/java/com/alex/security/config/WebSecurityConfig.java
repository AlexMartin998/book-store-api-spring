package com.alex.security.config;

import com.alex.security.auth.jwt.JwtAuthEntryPoint;
import com.alex.security.auth.jwt.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// toda app con Spring Security al Iniciar Siempre buscara el   SecurityFilterChain   q configura toda la seguridad http de la app


@Configuration
@EnableWebSecurity // estas 2 @ deben ir juntas en Spring Boot 3.0
@EnableMethodSecurity(securedEnabled = true)    // enable authorization
@RequiredArgsConstructor
public class WebSecurityConfig {

    // Autowired en auto xq son FINAL <-- @RequiredArgsConstructor
    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;    // <-- JwtAppConfig
    private final JwtAuthEntryPoint jwtAuthEntryPoint;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()
                    .authenticationEntryPoint(jwtAuthEntryPoint)    // handle Spring Security Error Response / customize error msg
                .and()
                .authorizeHttpRequests()
                    .requestMatchers("/api/v1/auth/**", "/api/v1/free/**")
                    .permitAll() //white list
                    .anyRequest()
                        .authenticated()
                .and()
                    .sessionManagement()    // stateless: elimina las sessiones para q con c/request se valide el auth
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authenticationProvider(authenticationProvider)  // indica a Spring q auth Provider debe usar
                    .addFilterBefore(
                        // se ejecuta ANTES del UsernamePasswordAuth... filter xq necesitamos verificar el jwt antes de actualizar el SecurityContextHolder
                        jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class
                    );

        return http.build();
    }
}

