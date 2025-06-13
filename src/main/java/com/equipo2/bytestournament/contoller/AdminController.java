package com.equipo2.bytestournament.contoller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
// import org.apache.catalina.connector.Response;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/admin")
public class AdminController {
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String dashboard() {
        return "Panel de administración: ";
    }
    
    // TODO: Implementar el método para crear un usuario, necesario el UsuarioDTO
    // @PostMapping("/user")
    // @PreAuthorize("hasAutority('USER_CREATE')")
    // public ResponseEntity<?> postMethodName(@RequestBody UsuarioDTO dto) {
    //     return ResponseEntity.ok().build();;
    // }
    
}
