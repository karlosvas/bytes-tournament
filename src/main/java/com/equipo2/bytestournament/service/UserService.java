package com.equipo2.bytestournament.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.equipo2.bytestournament.DTO.UserDTO;
import com.equipo2.bytestournament.config.JwtUtil;
import com.equipo2.bytestournament.enums.ApiResponse;
import com.equipo2.bytestournament.enums.Rank;
import com.equipo2.bytestournament.enums.Role;
import com.equipo2.bytestournament.exceptions.RequestException;
import com.equipo2.bytestournament.mapper.UserMapper;
import com.equipo2.bytestournament.model.User;
import com.equipo2.bytestournament.repository.UserRepository;

/**
 * UserService es un servicio que se encarga de gestionar las operaciones relacionadas con los usuarios.
 * Proporciona métodos para registrar usuarios, iniciar sesión, obtener datos del perfil y más.
 * 
 * {@link Service} es una anotación de Spring que indica que esta clase es un servicio de Spring
 * y será utilizada para realizar operaciones de negocio relacionadas con los usuarios.
 */
@Service
public class UserService {

    /**
     * userRepository Repositorio para acceder a los usuarios.
     * userMapper Mapeador para convertir entre User y UserDTO.
     * jwtUtil Utilidad para generar y validar tokens JWT.
     * passwordEncoder Codificador de contraseñas para cifrar las contraseñas de los usuarios.
     * authenticationManager Gestor de autenticación para autenticar usuarios.
     * Logger para registrar mensajes de error y depuración.
     */
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    public UserService(AuthenticationManager authenticationManager, UserRepository userRepository, UserMapper userMapper, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * Este método verifica si el usuario tiene el rol de ADMIN y si la autenticación es
     * proporcionada, asegurándose de que solo los usuarios ADMIN puedan crear otros usuarios ADMIN.
     * Además, asigna un rango al usuario en base a sus puntos y guarda el usuario en la base de datos.
     * Si la autenticación es exitosa, genera y devuelve un token JWT.
     * 
     * @param userDTO el DTO del usuario que se va a registrar
     * @param authenticationRequest la autenticación del usuario que está intentando registrar a otro usuario
     * @return un token JWT si el registro es exitoso
     */
    public String registerUser(UserDTO userDTO, Authentication authenticationRequest) {
        try {
            // Comprobamos que el que crea un usuario solo cree admin si es admin
            if(userDTO.getRole().equals(Role.ADMIN)) {
                // Si hay autentificación deve de ser administrador
                if(authenticationRequest != null){
                    String userName = authenticationRequest.getName(); // Email
                    Optional<User> userRequestOptional = userRepository.findByEmail(userName);
                    if(userRequestOptional.isEmpty())
                    throw new RequestException(ApiResponse.NOT_FOUND, "Usuario no encontrado", "No se encontro un usuario con ese email");
                    
                    User userRequest = userRequestOptional.get();
                    if(!userRequest.getRole().equals(Role.ADMIN)) 
                        throw new RequestException(ApiResponse.FORBIDDEN, "No tienes permisos para crear un usuario ADMIN", "Solo los usuarios ADMIN pueden crear otros usuarios ADMIN para saber los tipos de usuarios que pueden crear ve el enum Role");
                } else {
                    // Si no hay autentificación, no se puede crear un usuario ADMIN
                    throw new RequestException(ApiResponse.FORBIDDEN, "No tienes permisos para crear un usuario ADMIN", "Solo los usuarios ADMIN pueden crear otros usuarios ADMIN para saber los tipos de usuarios que pueden crear ve el enum Role");
                }
            }

            // Le damos el rango al usuario en base a sus puntos
            userDTO.setRank(Rank.fromPoints(userDTO.getPoints()));
            
            Optional<User> userSearch = userRepository.findByUsername(userDTO.getUsername());

            // Si el usuario ya existe, lanzamos una excepción ya que no puede haver registro duplicado
            if(userSearch.isPresent()) 
                throw new RequestException(ApiResponse.DUPLICATE_RESOURCE);
            

            // UserDTO -> User
            User user = userMapper.userDTOToUser(userDTO);
            
            // Ciframos la contraseña
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            logger.info("Password encoded for user: " + user.getEmail());

            // Guardar en la base de datos el User
            User newUser = userRepository.save(user);
            System.out.println("User saved: " + newUser.getEmail());


            // Creamos el objecto authentication
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    userDTO.getEmail(),
                    userDTO.getPassword()
                )
            );
            
            // Generar y devolver JWT 
            return jwtUtil.generateToken(authentication);
        } catch (RequestException e) {
            // Si el usuario ya existe o hay otro problema relacionado con la solicitud
            throw e; // Re-lanzamos la excepción para que sea manejada por el controlador
        } catch (Exception e) {
             // Si las credenciales son inválidas o hay otro problema
            throw new RequestException(ApiResponse.INTERNAL_SERVER_ERROR);
        }
       
    }
   
    /**
     * Inicia sesión de un usuario con las credenciales proporcionadas.
     * Este método autentica al usuario usando sus credenciales (email y contraseña) y,
     * si la autenticación es exitosa, genera y devuelve un token JWT.
     * 
     * @param userDTO el DTO del usuario que contiene el email y la contraseña
     * @return un token JWT si la autenticación es exitosa
     */
    public String userLogin(UserDTO userDTO) throws RequestException {
        try {
            // Autenticar al usuario usando las credenciales proporcionadas
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    userDTO.getEmail(),      // Email proporcionado por el usuario
                    userDTO.getPassword()    // Contraseña proporcionada por el usuario
                )
            );
            
            // Si la autenticación es exitosa (no lanza excepción), genera un token JWT
            return jwtUtil.generateToken(authentication);
            
        } catch (Exception e) {
            // Si las credenciales son inválidas o hay otro problema
            throw new RequestException(ApiResponse.AUTHENTICATION_FAILED);
        }
    }

    /**
     * Obtiene los datos del perfil del usuario autenticado.
     * Este método utiliza la autenticación proporcionada para buscar al usuario en la base de datos
     * y devolver un UserDTO con sus datos.
     * 
     * @param authentication la autenticación del usuario que está solicitando sus datos de perfil
     * @return un UserDTO con los datos del usuario autenticado
     */
    public UserDTO profileData(Authentication authentication) {
        try {
            // Obtener de la base de datos el User, pasandole el id de DTO
            String email = authentication.getName();
            Optional<User> newUser = userRepository.findByUsername(email);//Se cambia de email a username
            
            // Si no existe devolvemos un error
            if(!newUser.isPresent())
                throw new Exception();

            // Convertimos a User -> UserDTO y lo devolvemos
            UserDTO user = userMapper.userToUserDTO(newUser.get());
            return user;
        } catch (Exception e) {
             throw new RequestException(ApiResponse.BAD_REQUEST);
        }
    
    }

   /**
    * Obtiene el perfil de un usuario por su ID.
    * Este método busca al usuario en la base de datos utilizando su ID y devuelve un UserDTO
    * con sus datos. Si el usuario no existe, lanza una excepción.

    * @param userID el ID del usuario cuyo perfil se desea obtener
    * @return un UserDTO con los datos del usuario
    */
    public UserDTO profileUser(Long userID) {
        try {
            // Cojemos de la base de datos el usuario con el id de userDTO
            Optional<User> newUserOptional = userRepository.findById(userID);
            
            // Si no existe error
            if(!newUserOptional.isPresent())
                throw new Exception();
            
            // Devolvemos el DTO asociado
            UserDTO user = userMapper.userToUserDTO(newUserOptional.get());
            return user;
        } catch (Exception e) {
          throw new RequestException(ApiResponse.BAD_REQUEST);
        }
    }

    /**
     * Obtiene el nombre de usuario a partir de la autenticación.
     * Este método busca al usuario en la base de datos utilizando el nombre de usuario (email
     * de la autenticación) y devuelve su nombre de usuario.
     * 
     * @param authentication la autenticación del usuario que está solicitando su nombre de usuario
     * @return el nombre de usuario del usuario autenticado
     */
    public String getUserNameFronAutentication(Authentication authentication) {
        // Obtenemos el usuario de la base de datos a partir de el token de autenticación del usuario que hizo la petición
        Optional<User> userOptional =  userRepository.findByEmail(authentication.getName());
        
        if(userOptional.isEmpty())
            throw new RequestException(ApiResponse.NOT_FOUND, "Usuario no encontrado", "No se encontro un usuario con esa ID");
        
        User user = userOptional.get();
        return user.getUsername();
    }

     /**
     * Obtiene la lista de todos los usuarios registrados en el sistema.
     * Este método recupera todos los usuarios de la base de datos, los convierte a UserDTO
     * y devuelve la lista resultante.
     * 
     * @return List<UserDTO> que contiene la lista de todos los usuarios encontrados.
     */
    public List<UserDTO> listAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOs = userMapper.userListToUserDTOList(users);
        return userDTOs;
    }
}
