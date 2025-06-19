package com.equipo2.bytestournament.contoller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.equipo2.bytestournament.DTO.UserDTO;
import com.equipo2.bytestournament.service.CustomUserDetailsService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

// Checklist Login
// implementar POST /api/auth/login que valida credenciales y devuelve JWT.

// Checklist Datos Propios
// implementar POST /api/users/me accesible para PLAYER y ADMIN.


// Checklist Perfil Público
// implementar POST /api/users/{id} accesible para cualquier usuario.

@RestController
@RequestMapping("/api")
public class UserController {
    
    private final CustomUserDetailsService customUserDetailsService;

    public UserController(CustomUserDetailsService customUserDetailsService){
        this.customUserDetailsService=customUserDetailsService;
    }

    
    @PostMapping("/auth/register")
    public String registerUser(@RequestBody UserDTO entity) {
        return customUserDetailsService.registerUser(entity);
    }

    @PostMapping("/auth/login")
    public String userLogin(@RequestBody UserDTO entity) {
        return customUserDetailsService.userLogin(entity);
    }


    @PostMapping("/users/me")
    @PreAuthorize("hasRole('ROLE_PLAYER', 'ROLE_ADMIN')")
    public UserDTO personalData(@PathVariable Authentication authentication) {
        return customUserDetailsService.profileData(authentication);
    }

    @PostMapping("user/{id}")
    public UserDTO profileUser(@PathVariable Long id) {
        return customUserDetailsService.profileUser(id);
    }
}