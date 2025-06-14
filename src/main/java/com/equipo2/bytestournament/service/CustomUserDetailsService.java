package com.equipo2.bytestournament.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * CustomUserDetailsService es una implementación de UserDetailsService que se utiliza para cargar los detalles del usuario desde la base de datos.
 * Esta clase es responsable de buscar un usuario por su nombre de usuario (email) y construir un objeto UserDetails que Spring Security utilizará para la autenticación y autorización.
 * @Service Anotación que indica que esta clase es un servicio de Spring.
 */
@Service
// TODO: Necesario implementar  UserRepository
public class CustomUserDetailsService implements UserDetailsService {
    /**
     * userRepository es una instancia del repositorio de usuarios que se utiliza para acceder a los datos del usuario en la base de datos.
     */
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // TODO: Necesario implementar UserRepository y el modelo de usuario
    /**
     * loadUserByUsername es un método que busca un usuario por su nombre de usuario (email) y devuelve un objeto UserDetails, se ejecuta automaticamente por Spring Security durante el proceso de autenticación en el filtros JwtAuthentication Filter.
     * @param username El nombre de usuario (email) del usuario a buscar.
     * @return Un objeto UserDetails que contiene la información del usuario, incluyendo su nombre de usuario, contraseña y roles.
     * @throws UsernameNotFoundException Si no se encuentra un usuario con el nombre de usuario proporcionado.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);

        if(!user.isPresent())
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        
        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toList()))
                .build();
    }
    
}