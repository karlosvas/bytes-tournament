package com.equipo2.bytestournament.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.equipo2.bytestournament.repository.UserRepository;
import java.util.HashSet;
import java.util.Optional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.equipo2.bytestournament.DTO.UserDTO;
import com.equipo2.bytestournament.config.JwtUtil;
import com.equipo2.bytestournament.enums.ApiResponse;
import com.equipo2.bytestournament.model.Users;
import com.equipo2.bytestournament.enums.AuthorityPrivilegies;
import com.equipo2.bytestournament.exceptions.RequestException;
import com.equipo2.bytestournament.mapper.UserMapper;
import org.springframework.security.core.Authentication;

/**
 * CustomUserDetailsService es una implementación de UserDetailsService que se utiliza para cargar los detalles del usuario desde la base de datos.
 * Esta clase es responsable de buscar un usuario por su nombre de usuario (email) y construir un objeto UserDetails que Spring Security utilizará para la autenticación y autorización.
 * @Service Anotación que indica que esta clase es un servicio de Spring.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    /**
     * userRepository es una instancia del repositorio de usuarios que se utiliza para acceder a los datos del usuario en la base de datos.
     */
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;


    public CustomUserDetailsService(UserRepository userRepository, UserMapper userMapper, JwtUtil jwtUtil, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public String registerUser(UserDTO userDTO){
        try {
             // UserDTO -> User
            Users user = userMapper.userDtoToUser(userDTO);

            // Ciframos la contraseña
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            
            // Guardar en la base de datos el User
            Users newUser = userRepository.save(user);

            // Creamos el objecto authentication
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    newUser.getEmail(),
                    newUser.getPassword()
                )
            );
            
            // Generar y devolver JWT 
            return jwtUtil.generateToken(authentication);
        } catch (Exception e) {
             // Si las credenciales son inválidas o hay otro problema
            throw new RequestException(ApiResponse.AUTHENTICATION_FAILED);
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
            Optional<Users> newUser = userRepository.findByEmail(email);
            
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
            Optional<Users> newUserOptional = userRepository.findById(id);
            
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
     * loadUserByUsername es un método que busca un usuario por su nombre de usuario (email) y devuelve un objeto UserDetails, se ejecuta automaticamente por Spring Security durante el proceso de autenticación en el filtros JwtAuthentication Filter.
     * @param username El nombre de usuario (email) del usuario a buscar.
     * @return Un objeto UserDetails que contiene la información del usuario, incluyendo su nombre de usuario, contraseña y roles.
     * @throws UsernameNotFoundException Si no se encuentra un usuario con el nombre de usuario proporcionado.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> userOptional = userRepository.findByEmail(username);

        if(!userOptional.isPresent())
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        Users user = userOptional.get();
            
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(new SimpleGrantedAuthority(user.getRole().getName()))
                .build();
    }

    public void addAuthorityToUser(Long userId, AuthorityPrivilegies authority) {
        Optional<Users> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        
        Users existingUser = userOptional.get();
        if (existingUser.getAuthorityPrivilegies() == null) {
            existingUser.setAuthorityPrivilegies(new HashSet<>());
        }
        existingUser.getAuthorityPrivilegies().add(authority);
        userRepository.save(existingUser);
    }
}