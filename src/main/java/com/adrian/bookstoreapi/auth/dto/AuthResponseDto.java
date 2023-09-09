package com.adrian.bookstoreapi.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDto {

    private String token;
    private UserDto user;

    @Data
    public static class UserDto {
        private long id;
        private String firstname;
        private String lastname;
        private String email;
        private RoleDto role;
    }

    @Data
    public static class RoleDto {
        private int id;
        private String name;
    }

}

