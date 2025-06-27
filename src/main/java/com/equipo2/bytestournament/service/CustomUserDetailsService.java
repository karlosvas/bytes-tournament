package com.equipo2.bytestournament.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.equipo2.bytestournament.repository.UserRepository;
import java.util.HashSet;
import java.util.Optional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.equipo2.bytestournament.model.User;
import com.equipo2.bytestournament.DTO.UserDTO;
import com.equipo2.bytestournament.enums.ApiResponse;
import com.equipo2.bytestournament.enums.AuthorityPrivilegies;
import com.equipo2.bytestournament.exceptions.RequestException;
import com.equipo2.bytestournament.mapper.UserMapper;

/**
 * CustomUserDetailsService es una implementación de UserDetailsService que se utiliza para cargar los detalles del usuario desde la base de datos.
 * Esta clase es responsable de buscar un usuario por su nombre de usuario (email) y construir un objeto UserDetails que Spring Security utilizará para la autenticación
 * y autorización.
 * 
 * {@link Service} Anotación que indica que esta clase es un servicio de Spring.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    /**
     * userRepository es una instancia del repositorio de usuarios que se utiliza para acceder a los datos del usuario en la base de datos.
     * userMapper es una instancia del mapeador que se utiliza para convertir entre entidades User y UserDTO.
     */
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public CustomUserDetailsService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }
 
    /**
     * loadUserByUsername es un método que carga un usuario por su nombre de usuario (email) y devuelve un objeto UserDetails, se ejecuta automaticamente 
     * por Spring Security durante el proceso de autenticación en el filtros JwtAuthentication Filter.
     * 
     * @param username El nombre de usuario (email) del usuario a buscar.
     * @return Un objeto UserDetails que contiene la información del usuario, incluyendo su nombre de usuario, contraseña y roles.
     * @throws UsernameNotFoundException Si no se encuentra un usuario con el nombre de usuario proporcionado.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Lo obtenemos de la base de datos
        Optional<User> userOptional = userRepository.findByUsername(username);

        // Verificamos si el usuario existeq
        if(!userOptional.isPresent())
            throw new RequestException(ApiResponse.NOT_FOUND, "Usuario no encontrado", "No se encontró un usuario con el nombre proporcionado: " + username);
        
        // Si existe, lo convertimos a UserDetails y lo devolvemos
        User user = userOptional.get();
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(new SimpleGrantedAuthority(user.getRole().getName()))
                .build();
    }

    /**
     * Agrega una autoridad específica a un usuario dado su ID.
     * Este método busca al usuario por su ID, verifica si existe y luego agrega la autoridad especificada a su conjunto de privilegios.
     * 
     * @param userId El ID del usuario al que se le agregará la autoridad.
     * @param authority La autoridad que se agregará al usuario, representada por la enumeración AuthorityPrivilegies.
     */
    public UserDTO addAuthorityToUser(Long userId, AuthorityPrivilegies authority) {
        // Verificamos si el usuario existe
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) 
            throw new RequestException(ApiResponse.NOT_FOUND, "Usuario no encontrado", "No se encontró un usuario con el ID proporcionado: " + userId);
        
        // Si existe, lo obtenemos
        User existingUser = userOptional.get();
        if (existingUser.getAuthorityPrivilegies() == null) 
            existingUser.setAuthorityPrivilegies(new HashSet<>());
        
        // Verificamos si la autoridad ya existe
        existingUser.getAuthorityPrivilegies().add(authority);
        User user = userRepository.save(existingUser);
        return userMapper.userToUserDTO(user);
    }
}