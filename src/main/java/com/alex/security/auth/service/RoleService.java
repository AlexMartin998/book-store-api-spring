package com.alex.security.auth.service;

import com.alex.security.auth.entity.Role;


public interface RoleService {

    Role findOneByName(String name);

}
