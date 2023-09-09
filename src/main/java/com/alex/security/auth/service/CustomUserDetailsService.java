package com.alex.security.auth.service;

import com.alex.security.auth.jwt.UserDetailsImpl;
import com.alex.security.users.entity.Usuario;
import com.alex.security.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    // @Autowired all FinalProps in auto by constructor thanks to @RequiredArgsConstructor
    private final UserService userService;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario user = userService.findOneByEmail(email);

        return new UserDetailsImpl(user);
    }

}
