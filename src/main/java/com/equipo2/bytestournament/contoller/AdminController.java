package com.equipo2.bytestournament.contoller;

import org.springframework.web.bind.annotation.RequestMapping;
import com.equipo2.bytestournament.service.CustomUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.equipo2.bytestournament.enums.AuthorityPrivilegies;
import com.equipo2.bytestournament.DTO.UserDTO;
import com.equipo2.bytestournament.annotations.SwaggerApiResponses;
import com.equipo2.bytestournament.enums.ApiResponse;

/**
 * AdminController es un controlador REST que maneja las solicitudes relacionadas con la administración de usuarios.
 * Este controlador está protegido por roles y permisos específicos, asegurando que solo los usuarios autorizados puedan acceder a sus métodos.
 * no es necesario utilizar PreAuthorize("hasAnyAuthority('ADMIN')") porque ya esta protegida la ruta solo para admin desde la configuración de seguridad.
 * 
 * {@link Tag} Anotación que proporciona información adicional para la documentación de Swagger.
 * {@link SwaggerApiResponses} Anotación que proporciona información adicional para la documentación de Swagger.
 * {@link Operation} Anotación que describe la operación de un endpoint específico.
 * 
 * {@link RestController} Anotación que indica que esta clase es un controlador REST.
 * {@link RequestMapping} ("/admin") Define la ruta base para todas las solicitudes manejadas por este controlador.
 * {@link PreAuthorize} Anotación que especifica que ciertos métodos solo pueden ser accedidos por usuarios con roles específicos.
 * 
 * {@link GetMapping} Bean de Spring que define un método que maneja las solicitudes GET
 * {@link PostMapping} Bean de Spring que define un método que maneja las solicitudes POST
 * {@link DeleteMapping} Bean de Spring que define un método que maneja las solicitudes DELETE
 * {@link PutMapping} Bean de Spring que define un método que maneja las solicitudes PUT
 */
@Tag(name = "Admin", description = "Controlador para la administración de usuarios y permisos")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    /**
     * CustomUserDetailsService es un servicio que maneja la lógica de negocio relacionada con los usuarios.
     * Este servicio se utiliza para agregar permisos a los usuarios y realizar otras operaciones administrativas.
     */
    private final CustomUserDetailsService userService;

    public AdminController(CustomUserDetailsService userService) {
        this.userService = userService;
    }

    /**
     * Accede al dashboard de administración.
     * 
     * @return ResponseEntity<String> Un objeto ResponseEntity que contiene un mensaje de éxito y un estado HTTP 200 OK.
     */
    @SwaggerApiResponses
    @Operation(summary = "Obtener el dashboard de administración", description = "Este endpoint permite a los administradores acceder al dashboard de administración.")
    @GetMapping("/dashboard")
    public ResponseEntity<String> dashboard() {
        // TODO: Implementar la lógica del dashboard de administración
        return ResponseEntity.ok("Admin Dashboard");
    }

    /**
     * Permite a un administrador dar el permiso 'USER_CREATE' a un usuario específico existente, pasado por parámetro.
     * 
     * @param username El nombre de usuario del nuevo usuario a crear.
     * @return ResponseEntity<UserDTO> Un objeto ResponseEntity que contiene el nuevo usuario creado para devolber 201 Created.
     */
    @PostMapping("/user")
    @PreAuthorize("hasAuthority('USER_CREATE')")
    @Operation(summary = "Crear un nuevo usuario", description = "Este endpoint permite a los administradores dar el permiso 'USER_CREATE' a un usuario específico existente.")
    public ResponseEntity<UserDTO> postMethodName(@RequestBody Long username) {
        UserDTO newUserCreated = userService.addAuthorityToUser(username, AuthorityPrivilegies.USER_CREATE);
        return ResponseEntity.status(ApiResponse.CREATED.getStatus()).body(newUserCreated);
    }
}

