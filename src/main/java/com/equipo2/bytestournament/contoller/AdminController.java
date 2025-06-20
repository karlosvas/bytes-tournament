package com.equipo2.bytestournament.contoller;

import com.equipo2.bytestournament.service.CustomUserDetailsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.equipo2.bytestournament.enums.AuthorityPrivilegies;

/**
 * AdminController es un controlador REST que maneja las solicitudes relacionadas con la administración de usuarios.
 * Este controlador está protegido por roles y permisos específicos, asegurando que solo los usuarios autorizados puedan acceder a sus métodos.
 * @RestController Anotación que indica que esta clase es un controlador REST.
 * @RequestMapping("/admin") Define la ruta base para todas las solicitudes manejadas por este controlador.
 * /admin Ruta para usuarios con rol ADMIN especificado en la configuración de seguridad
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {


    private final CustomUserDetailsService userService;


    public AdminController(CustomUserDetailsService userService) {
        this.userService = userService;
    }


    /**
     * dashboard es un método que maneja las solicitudes GET a la ruta "/dashboard".
     * @return ResponseEntity<String> Un objeto ResponseEntity que contiene un mensaje de éxito y un estado HTTP 200 OK.
     */
    @GetMapping("/dashboard")
    public ResponseEntity<String> dashboard() {
        // TODO: Implementar la lógica del dashboard de administración
        return ResponseEntity.ok("Admin Dashboard");
    }

    /**
     * postMethodName es un método que maneja las solicitudes POST a la ruta "/user".
     * {@link PreAuthorize} Anotación que especifica, este método solo puede ser accedido por usuarios con el permiso "USER_CREATE".
     */
    @PostMapping("/user")
    @PreAuthorize("hasAuthority('USER_CREATE')")
    public ResponseEntity<?> postMethodName(@RequestBody Long username) {
        userService.addAuthorityToUser(username, AuthorityPrivilegies.USER_CREATE);
        return ResponseEntity.ok().build();
    }
}

