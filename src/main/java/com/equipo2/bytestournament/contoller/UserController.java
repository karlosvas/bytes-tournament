package com.equipo2.bytestournament.contoller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.equipo2.bytestournament.DTO.UserDTO;
import com.equipo2.bytestournament.annotations.SwaggerApiResponses;
import com.equipo2.bytestournament.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;


@RestController
@RequestMapping("/api")
public class UserController {
    
    private final UserService userService;

    public UserController(UserService customUserDetailsService){
        this.userService=customUserDetailsService;
    }

    
    @SwaggerApiResponses
    @PostMapping("/auth/register")
    public String registerUser(@RequestBody UserDTO entity) {
        return userService.registerUser(entity);
    }

    @SwaggerApiResponses
    @GetMapping("/auth/login")
    public String userLogin(@RequestBody UserDTO entity) {
        return userService.userLogin(entity);
    }

    @SwaggerApiResponses
    @GetMapping("/user/me")
    @PreAuthorize("hasAnyAuthority('PLAYER', 'ADMIN')")
    public UserDTO personalData(Authentication authentication) {
        return userService.profileData(authentication);
    }

    @SwaggerApiResponses
    @GetMapping("/user/{id}")
    public UserDTO profileUser(@PathVariable Long id) {
        return userService.profileUser(id);
    }
}