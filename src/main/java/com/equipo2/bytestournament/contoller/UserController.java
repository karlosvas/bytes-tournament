package com.equipo2.bytestournament.contoller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.equipo2.bytestournament.DTO.UserDTO;
import com.equipo2.bytestournament.service.UserService;
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

    
    @PostMapping("/auth/register")
    public String registerUser(@RequestBody UserDTO entity) {
        return userService.registerUser(entity);
    }

    @PostMapping("/auth/login")
    public String userLogin(@RequestBody UserDTO entity) {
        return userService.userLogin(entity);
    }


    @PostMapping("/users/me")
    @PreAuthorize("hasAnyRole('PLAYER', 'ADMIN')")
    public UserDTO personalData(@PathVariable Authentication authentication) {
        return userService.profileData(authentication);
    }

    @PostMapping("user/{id}")
    public UserDTO profileUser(@PathVariable Long id) {
        return userService.profileUser(id);
    }
}