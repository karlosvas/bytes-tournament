package com.equipo2.bytestournament.service;

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
import com.equipo2.bytestournament.exceptions.RequestException;
import com.equipo2.bytestournament.mapper.UserMapper;
import com.equipo2.bytestournament.model.User;
import com.equipo2.bytestournament.repository.UserRepository;

@Service
public class UserService {

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


    public String registerUser(UserDTO userDTO) {
        try {
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

    public UserDTO profileData(Authentication authentication) {
        try {
            // Obtener de la base de datos el User, pasandole el id de DTO
            String email = authentication.getName();
            Optional<User> newUser = userRepository.findByEmail(email);
            
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

   
    public UserDTO profileUser(Long id) {
        try {
            // Cojemos de la base de datos el usuario con el id de userDTO
            Optional<User> newUserOptional = userRepository.findById(id);
            
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

    public String getUserNameFronAutentication(Authentication authentication) {
        Optional<User> userOptional =  userRepository.findByEmail(authentication.getName());
        
        if(userOptional.isEmpty())
            throw new RequestException(ApiResponse.NOT_FOUND, "Usuario no encontrado", "No se encontro un usuario con esa ID");
        
        User user = userOptional.get();
        return user.getUsername();
    }
}
