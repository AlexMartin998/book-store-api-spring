package com.adrian.bookstoreapi.auth.service;

import com.adrian.bookstoreapi.auth.entity.Role;


public interface RoleService {

    Role findOneByName(String name);

}
