package com.alex.security.auth.controller;

import com.alex.security.common.constants.RoleConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1")
public class DemoController {

    // no secured
    @GetMapping("/free")
    public ResponseEntity<?> free() {
        return ResponseEntity.ok("Hello World Not Secured");
    }

    // authentication required
    @GetMapping("secured")
    public ResponseEntity<?> secured(Authentication authentication) {
        String authUserEmail = ((UserDetails) authentication.getPrincipal()).getUsername(); // (email/uuid/username)

        return ResponseEntity.ok("Hello ".concat(authUserEmail));
    }

    // authorization required
    @GetMapping("secured-admin")
    @Secured(RoleConstants.ADMIN)
    public ResponseEntity<?> securedAdmin(Authentication authentication) {
        String authUserEmail = ((UserDetails) authentication.getPrincipal()).getUsername(); // (email/uuid/username)

        return ResponseEntity.ok("Hello ADMIN ".concat(authUserEmail));
    }

    // authorization required (several roles)
    @GetMapping("secured-roles")
    @Secured({RoleConstants.ADMIN, RoleConstants.USER})
    public ResponseEntity<?> securedRoles(Authentication authentication) {
        String authUserEmail = ((UserDetails) authentication.getPrincipal()).getUsername(); // (email/uuid/username)

        return ResponseEntity.ok("Hello Allowed Role ".concat(authUserEmail));
    }

}
