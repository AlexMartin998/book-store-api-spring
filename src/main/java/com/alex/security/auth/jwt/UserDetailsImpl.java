package com.alex.security.auth.jwt;

import com.alex.security.auth.entity.Role;
import com.alex.security.users.entity.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private final Usuario user;


    // Roles
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return mapRoles(user.getRoles());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();     // with we want to work (email, uuid, username)
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isDeleted();
    }


    // map all user roles
    private Collection<? extends GrantedAuthority> mapRoles(Set<Role> roles) {
        return roles.stream().map(rol -> new SimpleGrantedAuthority(rol.getName().name())).collect(Collectors.toSet());
    }

}


