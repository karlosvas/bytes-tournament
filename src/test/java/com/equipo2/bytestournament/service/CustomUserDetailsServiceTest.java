package com.equipo2.bytestournament.service;

import com.equipo2.bytestournament.DTO.UserDTO;
import com.equipo2.bytestournament.enums.AuthorityPrivilegies;
import com.equipo2.bytestournament.exceptions.RequestException;
import com.equipo2.bytestournament.mapper.UserMapper;
import com.equipo2.bytestournament.model.User;
import com.equipo2.bytestournament.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.HashSet;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Clase de prueba para UserService utilizando Mockito.
 * Esta clase contiene pruebas unitarias para los métodos de registro, inicio de sesión y obtención de datos del perfil de usuario.
 *
 * {}@link Mock} se utiliza para simular las dependencias de UserService, como AuthenticationManager, UserRepository, UserMapper, JwtUtil y PasswordEncoder.
 * {@link InjectMocks} se utiliza para inyectar los mocks en una instancia de UserService.
 * {@link MockitoAnnotations} se utiliza para inicializar los mocks antes de cada prueba.
 * {@link Test} se utiliza para marcar los métodos de prueba.
 */
public class CustomUserDetailsServiceTest {
    // Mokeamos las dependencias necesarias para la clase CustomUserDetailsService
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    public CustomUserDetailsServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Prueba para el método addAuthorityCreateAdmin.
     * Este método agrega una autoridad de creación de administrador a un usuario existente y devuelve un UserDTO.
     */
    @Test
    void addAuthorityCreateAdmin_usuarioExistente_agregaAutoridadYDevuelveDTO() {
        String username = "test@correo.com";
        AuthorityPrivilegies authority = AuthorityPrivilegies.USER_CREATE;
        User user = new User();
        user.setUsername(username);
        user.setAuthorityPrivilegies(new HashSet<>());
        UserDTO userDTO = new UserDTO();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.userToUserDTO(user)).thenReturn(userDTO);

        UserDTO result = customUserDetailsService.addAuthorityCreateAdmin(username, authority);

        assertEquals(userDTO, result);
        assertTrue(user.getAuthorityPrivilegies().contains(authority));
        verify(userRepository).save(user);
        verify(userMapper).userToUserDTO(user);
    }

    /**
     * Prueba para el método addAuthorityCreateAdmin cuando el usuario no existe.
     * Este método lanza una RequestException si el usuario no se encuentra en la base de datos.
     */
    @Test
    void addAuthorityCreateAdmin_usuarioNoExiste_lanzaExcepcion() {
        String username = "noexiste@correo.com";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(RequestException.class, () ->
                customUserDetailsService.addAuthorityCreateAdmin(username, AuthorityPrivilegies.USER_CREATE)
        );
    }
}
