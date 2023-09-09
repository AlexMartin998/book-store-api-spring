package com.adrian.bookstoreapi.users.service;

import com.adrian.bookstoreapi.users.entity.Usuario;


public interface UserService {

    Usuario create(Usuario user);

    Usuario findOneByEmail(String email);

}
