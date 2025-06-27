package com.equipo2.bytestournament.contoller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.equipo2.bytestournament.DTO.UserDTO;
import com.equipo2.bytestournament.annotations.SwaggerApiResponses;
import com.equipo2.bytestournament.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import com.equipo2.bytestournament.enums.ApiResponse;
import com.equipo2.bytestournament.config.SecurityConfig;

/**
 * UserController es un controlador REST que maneja las solicitudes relacionadas con la gestión de usuarios y autenticación.
 * Este controlador permite a los usuarios registrarse, iniciar sesión y acceder a sus datos personales.
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
@Tag(name = "User", description = "Controlador para la gestión de usuarios y autenticación")
@RestController
@RequestMapping("/api/user")
public class UserController {
    
    /**
     * UserService es un servicio que maneja la lógica de negocio relacionada con los usuarios.
     */
    private final UserService userService;

    public UserController(UserService customUserDetailsService){
        this.userService = customUserDetailsService;
    }

    /**
     * Maneja los registros de usuarios, para crear un nuevo usuario en la BD devolviendo el token de acceso.
     * Este método permite a un nuevo usuario registrarse en la aplicación, para crear otro administrador es necesario
     * tener un usuario con privilegios de administrador. Por ejemplo con el usuario root creado por defecto al iniciar la aplicación.
     * para mas info ver el metodo inMemoryUserDetailsManager en la clase {@link SecurityConfig}.
     * 
     * @param entity UserDTO que contiene la información del usuario a registrar, como nombre de usuario, contraseña y otros datos personales.
     * @param authentication Objeto Authentication que contiene la información de autenticación del usuario actual, utilizado para verificar los privilegios del usuario que realiza el registro.
     * @return ResponseEntity<String> que contiene el token de acceso del usuario registrado y un código de estado HTTP 201 creado si el registro es exitoso.
     */
    @SwaggerApiResponses
    @PostMapping("/auth/register")
    @Operation(summary = "Registrar un nuevo usuario", description = "Este endpoint permite a los usuarios registrarse en la aplicación, creando un nuevo usuario en la base de datos y devolviendo el token de acceso si el registro es exitoso. Para crear otro administrador es necesario tener un usuario con privilegios de administrador, como el usuario root creado por defecto al iniciar la aplicación.")
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserDTO entity, Authentication authentication) {
        return ResponseEntity.status(ApiResponse.CREATED.getStatus()).body(userService.registerUser(entity, authentication));
    }

    /**
     * Permite a los usuarios iniciar sesión en la aplicación.
     * Este método recibe un objeto UserDTO que contiene las credenciales del usuario (nombre de usuario y contraseña).
     * Si las credenciales son válidas, se devuelve un token de acceso que puede ser utilizado para autenticar futuras solicitudes.
     * 
     * @param entity UserDTO que contiene las credenciales del usuario (nombre de usuario y contraseña).
     * @return String que representa el token de acceso del usuario si las credenciales son válidas.
     */
    @SwaggerApiResponses
    @GetMapping("/auth/login")
    @Operation(summary = "Iniciar sesión", description = "Este endpoint permite a los usuarios iniciar sesión en la aplicación, proporcionando sus credenciales (nombre de usuario y contraseña) y obteniendo un token de acceso si las credenciales son válidas.")
    public String userLogin(@RequestBody @Valid UserDTO entity) {
        return userService.userLogin(entity);
    }

    /**
     * Este método permite a los usuarios autentificados obtener sus datos personales.
     * Este endpoint es accesible solo para usuarios autenticados con los roles de PLAYER o ADMIN.
     * 
     * @param authentication Authentication que contiene la información de autenticación del usuario actual, utilizado para verificar los privilegios del usuario que realiza la solicitud.
     * @return UserDTO que contiene los datos personales del usuario autenticado, como nombre de usuario, correo electrónico y otros datos relevantes.
     */
    @Operation(summary = "Obtener datos personales autentificados", description = "Este endpoint permite a los usuarios autentificados obtener sus datos personales.")
    @SwaggerApiResponses
    @GetMapping("/auth/me")
    @PreAuthorize("hasAnyRole('PLAYER', 'ADMIN')")
    public UserDTO personalData(Authentication authentication) {
        return userService.profileData(authentication);
    }

    /**
     * Permite a los usuarios obtener los datos personales de otro usuario por su ID.
     * Este método es accesible solo para usuarios con el rol de ADMIN.
     * 
     * @param userId representa el ID del usuario cuyo perfil se desea obtener.
     * @return UserDTO que contiene los datos personales del usuario con el ID especificado, como nombre de usuario, correo electrónico y otros datos relevantes.
     */
    @Operation(summary = "Obtener datos personales por id", description = "Este endpoint permite a los usuarios obtener los datos personales de el usuario con el id pasado por parametro, solo accesibles para usuarios con el rol de ADMIN.")
    @SwaggerApiResponses

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO profileUser(@PathVariable Long userId) {
        return userService.profileUser(userId);
    }

     /**
     * Obtiene la lista de todos los usuarios registrados.
     * Este método es accesible solo para usuarios con el rol de ADMIN.
     * 
     * @return List<UserDTO> que contiene la lista de todos los usuarios encontrados.
     */
    @Operation(summary = "Listar usuarios", description = "Este endpoint permite listar todos los usuarios.")
    @SwaggerApiResponses
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> listAllUsers() {
        return userService.listAllUsers();
    }

    /**
     * Actualiza los datos de un usuario existente.
     * Este método es accesible solo para usuarios con el rol de ADMIN.
     * 
     * @param id
     * @return
     */
    @SwaggerApiResponses
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar un usuario", description = "Este endpoint permite a los administradores eliminar un usuario por su ID.")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Actualiza los datos de un usuario existente.
     * Este método es accesible solo para usuarios con el rol de ADMIN.
     * 
     * @param id ID del usuario a actualizar.
     * @param userDTO UserDTO que contiene la información actualizada del usuario, como nombre de usuario, correo electrónico y otros datos relevantes.
     * @return UserDTO que contiene los datos actualizados del usuario.
     */
    @SwaggerApiResponses
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar un usuario", description = "Este endpoint permite a los administradores actualizar los datos de un usuario por su ID.")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody @Valid  UserDTO userDTO) {
        return userService.updateUser(id, userDTO);
    }
}