package com.equipo2.bytestournament.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.equipo2.bytestournament.DTO.UserDTO;
import com.equipo2.bytestournament.enums.Rank;
import com.equipo2.bytestournament.model.User;
import com.equipo2.bytestournament.repository.UserRepository;
import com.equipo2.bytestournament.mapper.UserMapper;
import com.equipo2.bytestournament.config.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.equipo2.bytestournament.enums.Role;
import org.springframework.security.core.Authentication;
import java.util.Optional;

/**
 * Clase de prueba para UserService utilizando Mockito.
 * Esta clase contiene pruebas unitarias para los métodos de registro, inicio de sesión y obtención de datos del perfil de usuario.
 *
 * {}@link Mock} se utiliza para simular las dependencias de UserService, como AuthenticationManager, UserRepository, UserMapper, JwtUtil y PasswordEncoder.
 * {@link InjectMocks} se utiliza para inyectar los mocks en una instancia de UserService.
 * {@link MockitoAnnotations} se utiliza para inicializar los mocks antes de cada prueba.
 * {@link Test} se utiliza para marcar los métodos de prueba.
 */
class UserServiceTest {
    // Test de la clase UserService con Mockito
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private PasswordEncoder passwordEncoder;

    // Datos de prueba, cremos un UserDTO y un User para simular el registro y el inicio de sesión
    private final UserDTO userDTO;
    private final User user;
    private final User userAdmin;
    private final UserDTO userAdminDTO;

    // Inyectamos los mocks en la instancia de UserService
    @InjectMocks
    private UserService userService;

    public UserServiceTest() {
        MockitoAnnotations.openMocks(this);
        user = new User().builder()
                .username("testUser")
                .password("encodedPassword")
                .email("test@gmail.com")
                .role(Role.PLAYER)
                .rank(Rank.BRONZE)
                .points(0)
                .build();
        userDTO = UserDTO.builder()
                .id(1L)
                .username("testUser")
                .password("testPassword")
                .email("test@gmail.com")
                .role(Role.PLAYER)
                .rank(Rank.BRONZE)
                .points(0)
                .build();

        userAdminDTO = UserDTO.builder()
                .id(2L)
                .username("adminUser")
                .password("adminPass")
                .email("admin@gmail.com")
                .role(Role.ADMIN)
                .rank(Rank.BRONZE)
                .points(0)
                .build();

        userAdmin = User.builder()
                .id(2L)
                .username("adminUser")
                .email("admin@gmail.com")
                .password("encodedPassword")
                .role(Role.ADMIN)
                .rank(Rank.BRONZE)
                .points(0)
                .build();
    }

    /**
     * Verifica que el método registerUser crea un nuevo usuario y devuelve un token JWT.
     */
    @Test
    void testRegisterUser() {
        // Simula el mapeo de UserDTO a User
        when(userMapper.userDTOToUser(userDTO)).thenReturn(user);
        // Simula el cifrado de la contraseña
        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("encodedPassword");
        // Simula el guardado del usuario en la base de datos
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Simula la autenticación del usuario
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        // Simula que el usuario que se registra no está autenticado ya uqe solo hace falta estarlo para crear administradores
        Authentication authenticationRequest = null;

        // Simula la generación del token JWT
        when(jwtUtil.generateToken(authentication)).thenReturn("mocked-jwt-token");

        // Ejecuta el metodo a probar
        String result = userService.registerUser(userDTO, authenticationRequest);

        // Verifica que el resultado no sea nulo y que el token sea el esperado
        assertNotNull(result);
        assertEquals("mocked-jwt-token", result);
        // Verifica que el usuario se haya guardado exactamente una vez
        verify(userRepository, times(1)).save(any(User.class));
    }

    /**
     * Verifica que el método userLogin autentica al usuario y devuelve un token JWT.
     */
    @Test
    void testUserLogin() {
        // Simula la autentificacion de uusario ue se obtiene al iniciar sesion o registrarse, cojendo el jwt que se le pasa para autentificarse
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        // Simula la generación del token JWT
        when(jwtUtil.generateToken(authentication)).thenReturn("mocked-jwt-token");

        // Ejecuta el metodo a probar
        String result = userService.userLogin(userDTO);

        // Verifica que el resultado no sea nulo y que el token sea el esperado
        assertNotNull(result);
        assertEquals("mocked-jwt-token", result);
    }

    /**
     * Verifica que el método registerUser y userLogin devuelven el mismo token JWT.
     */
    @Test
    void testTokensIgualesEnRegisterYLogin() {
        // Simula el mapeo de UserDTO a User
        when(userMapper.userDTOToUser(userDTO)).thenReturn(user);
        // Simula el cifrado de la contraseña
        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("encodedPassword");
        // Simula el guardado del usuario en la base de datos
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Simula la autentificacion de usuario que se obtiene al iniciar sesion o registrarse, cojendo el jwt que se le pasa para autentificarse
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        // Simula que el usuario que se registra no está autenticado ya uqe solo hace falta estarlo para crear administradores
        Authentication authenticationRequest = null;

        // Simula la generación del token JWT
        when(jwtUtil.generateToken(authentication)).thenReturn("mocked-jwt-token");

        // Ejecuta los métodos a probar
        String tokenRegister = userService.registerUser(userDTO, authenticationRequest);
        String tokenLogin = userService.userLogin(userDTO);

        // Verifica que ambos tokens sean iguales
        assertEquals(tokenRegister, tokenLogin);
    }

    /**
     * Verifica que el método profileData devuelve los datos del usuario autenticado.
     */
    @Test
    void testProfileData() {
        // Simula la autenticación del usuario (mock de Authentication)
        Authentication authentication = mock(Authentication.class);

        // Simula que el email del usuario autenticado es "test@gmail.com"
        when(authentication.getName()).thenReturn("test@gmail.com");
        // Simula que el repositorio devuelve un usuario al buscar por email
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(user));
        // Simula el mapeo de User a UserDTO
        when(userMapper.userToUserDTO(user)).thenReturn(userDTO);

        // Ejecuta el método a probar
        UserDTO result = userService.profileData(authentication);

        // Verifica que el resultado no sea nulo (es decir, que se devolvió un UserDTO)
        assertNotNull(result);
    }

    /**
     * Verifica que el método profileUser devuelve los datos del usuario por ID.
     */
    @Test
    void testProfileUser() {
        // Simula que el repositorio devuelve un usuario al buscar por ID
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        // Simula el mapeo de User a UserDTO
        when(userMapper.userToUserDTO(user)).thenReturn(userDTO);
        // Ejecuta el método a probar
        UserDTO result = userService.profileUser(1L);
        assertNotNull(result);
    }

    @Test
    void testRegisterUser_AdminCreatesAdmin() {
        // Simula que el usuario autenticado es ADMIN
        Authentication authenticationRequest = mock(Authentication.class);
        when(authenticationRequest.getName()).thenReturn("admin@gmail.com");

        when(userRepository.findByEmail("admin@gmail.com")).thenReturn(Optional.of(userAdmin));
        when(userRepository.findByUsername(userAdminDTO.getUsername())).thenReturn(Optional.empty());
        when(userMapper.userDTOToUser(userAdminDTO)).thenReturn(userAdmin);
        when(passwordEncoder.encode(userAdmin.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(userAdmin);

        // Simula la autenticación del nuevo usuario
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtUtil.generateToken(authentication)).thenReturn("mocked-jwt-token");

         // EJECUTA el método a probar
        String result = userService.registerUser(userAdminDTO, authenticationRequest);
        assertNotNull(result);
        verify(userRepository, times(1)).save(any(User.class)); 
   }
}