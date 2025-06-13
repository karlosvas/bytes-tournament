package com.equipo2.bytestournament.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
// import java.util.Optional;
// import org.springframework.security.core.userdetails.User;

@Service
// TODO: Necesario implementar  UserRepository
public class CustomUserDetailsService implements UserDetailsService {
    // private final UserRepository userRepository;

    // public CustomUserDetailsService(UserRepository userRepository) {
    //     this.userRepository = userRepository;
    // }

    // TODO: Implementar el método loadUserByUsername una vez que se tenga el UserRepository y el modelo de usuario
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Optional<User> user = userRepository.findByEmail(username);

        // if(!user.isPresent())
        //     throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        
        // return User.builder()
        //         .username(user.getEmail())
        //         .password(user.getPassword())
        //         .roles(user.getRoles())
        //         .build();
        return null; // Solución temporal
    }
    
}