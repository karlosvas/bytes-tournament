package com.equipo2.bytestournament.contoller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.equipo2.bytestournament.service.CustomUserDetailsService;


@RestController
@RequestMapping("/login")
public class UserController {

    CustomUserDetailsService uds;

    public UserController(CustomUserDetailsService uds) {
        this.uds = uds;
    }
    
    @PostMapping("/auth")
    public String postMethodName(@RequestBody String entity) {
        uds.loadUserByUsername(entity);
        return entity;
    }
    
}
