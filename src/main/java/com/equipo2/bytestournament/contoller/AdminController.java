package com.equipo2.bytestournament.contoller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * AdminController es un controlador REST que maneja las solicitudes relacionadas con la administración de usuarios.
 * Este controlador está protegido por roles y permisos específicos, asegurando que solo los usuarios autorizados puedan acceder a sus métodos.
 * @RestController Anotación que indica que esta clase es un controlador REST.
 * @RequestMapping("/admin") Define la ruta base para todas las solicitudes manejadas por este controlador.
 * /admin Ruta para usuarios con rol ADMIN especificado en la configuración de seguridad
 */
@RestController
@RequestMapping("/admin")
public class AdminController {
    /**
     * dashboard es un método que maneja las solicitudes GET a la ruta "/dashboard".
     * @return
     */
    @GetMapping("/dashboard")
    public ResponseEntity<?> dashboard() {
        // TODO: Implementar la lógica del dashboard de administración
        return ResponseEntity.ok("Admin Dashboard");
    }
    
    // TODO: Implementar el método para crear un usuario, necesario el UsuarioDTO
    /**
     * postMethodName es un método que maneja las solicitudes POST a la ruta "/user".
     * @PreAuthorize("hasAutority('USER_CREATE')") Anotación que especifica, este método solo puede ser accedido por usuarios con el permiso "USER_CREATE".
     */
    @PostMapping("/user")
    @PreAuthorize("hasAutority('USER_CREATE')")
    public ResponseEntity<?> postMethodName(@RequestBody UsuarioDTO dto) {
        return ResponseEntity.ok().build();;
    }
    
}
