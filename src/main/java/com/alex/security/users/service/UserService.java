package com.alex.security.users.service;

import com.alex.security.users.entity.Usuario;


public interface UserService {

    Usuario create(Usuario user);

    Usuario findOneByEmail(String email);

}
