package com.equipo2.bytestournament.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.equipo2.bytestournament.DTO.UserDTO;
import com.equipo2.bytestournament.contoller.UserController;
import com.equipo2.bytestournament.enums.Role;
import com.equipo2.bytestournament.mapper.UserMapper;
import com.equipo2.bytestournament.repository.UserRepository;
import com.equipo2.bytestournament.service.UserService;

/**
 * Clase de prueba para el controlador AdminController.
 * Esta clase verifica que las peticiones al controlador AdminController funcionen correctamente.
 *
 * {@link WebMvcTest} se utiliza para configurar un entorno de prueba centrado en el controlador, lo que permite simular
 * peticiones HTTP y verificar las respuestas sin necesidad de iniciar toda la aplicación.
 * {@link WithMockUser} se utiliza para simular un usuario autenticado con el rol ADMIN, lo que permite probar los
 * endpoints protegidos por roles de seguridad.
 * {@link MockBean} se utiliza para crear un mockear un servicio, lo que permite simular su comportamiento en las
 * pruebas sin necesidad de depender de la implementación real.
 * {@link Test} se utiliza para marcar los métodos de prueba, lo que permite que el framework de pruebas los ejecute automáticamente.
 * {@link WithMockUser} se utiliza para simular un usuario autenticado con el rol ADMIN,
 * {@link Autowired} se utiliza para inyectar dependencias en la clase de prueba,
 */
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserMapper userMapper;

    /**
     * Test para el registro de un usuario.
     * Este test verifica que al enviar una petición POST al endpoint /api/user/auth/register
     */
    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    void registerUserTest() throws Exception {
         String userJson = """
           {
            "username": "jorge",
            "email": "jorge@gmail.com",
            "password": "jorge20",
            "role": "PLAYER"
            }
        """;

        Mockito.when(userService.registerUser(Mockito.any(UserDTO.class), Mockito.any()))
                .thenReturn("ejemplojwt");

        mockMvc.perform(post("/api/user/auth/register")
                .with(csrf())
                .contentType("application/json")
                .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(content().string("ejemplojwt"));;
    }

    /**
     * Test para el inicio de sesión de un usuario.
     * Este test verifica que al enviar una petición GET al endpoint /api/user/auth/login
     * con las credenciales correctas, se reciba un JWT.
     */
    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    void userLoginTest() throws Exception {
        String loginJson = """
        {
            "username": "admin",
            "email": "admin@bytes.com",
            "password": "admin",
            "role": "ADMIN"
        }
        """;

        Mockito.when(userService.userLogin(Mockito.any(UserDTO.class)))
                .thenReturn("ejemplojwt");

        mockMvc.perform(get("/api/user/auth/login")
                .with(csrf())
                .contentType("application/json")
                .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(content().string("ejemplojwt"));
    }

    /**
     * Test para la obtención de los datos personales del usuario autenticado.
     * Este test verifica que al enviar una petición GET al endpoint /api/user/me
     * se reciban los datos del usuario autenticado.
     */
    @Test
    @WithMockUser(username = "testuser", roles = {"ADMIN"})
    void personalDataTest() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setEmail("testuser@correo.com");
        userDTO.setRole(Role.ADMIN);

        Mockito.when(userService.profileData(Mockito.any(Authentication.class))).thenReturn(userDTO);

        mockMvc.perform(get("/api/user/auth/me"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("testuser@correo.com"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    /**
     * Test para la obtención del perfil de un usuario específico.
     * Este test verifica que al enviar una petición GET al endpoint /api/user/{userId}
     * se reciban los datos del usuario con el ID especificado.
     */
    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    void profileUserTest() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setEmail("testuser@correo.com");
        userDTO.setRole(Role.ADMIN);

        Mockito.when(userService.profileUser(1L)).thenReturn(userDTO);

        mockMvc.perform(get("/api/user/{userId}", 1L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("testuser"))
            .andExpect(jsonPath("$.email").value("testuser@correo.com"))
            .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    /**
     * Test para la obtención de todos los usuarios.
     * Este test verifica que al enviar una petición GET al endpoint /api/user/{userId}/list
     */
    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    void getAllUsersTest() throws Exception {
        Long userId = 1L;

        Mockito.when(userService.listAllUsers()).thenReturn(List.of(new UserDTO()));

        mockMvc.perform(get("/api/user/list", userId)
                .with(csrf()))
                .andExpect(status().isOk());
    }

    /**
     * Test para la actualización de un usuario.
     * Este test verifica que al enviar una petición PUT al endpoint /api/user/{userId}/update
     */
    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    void updateUserTest() throws Exception {
        Long userId = 1L;
        String userJson = """
        {
            "username": "admin",
            "email": "admin@bytes.com",
            "password": "admin",
            "role": "ADMIN",
            "points": 900
        }
                    
        """;
        Mockito.when(userService.updateUser(Mockito.anyLong(), Mockito.any(UserDTO.class)))
                .thenReturn(new UserDTO());

        mockMvc.perform(put("/api/user/{userId}", userId).with(csrf())
                .contentType("application/json")
                .content(userJson))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    void deleteUserTest() throws Exception {
        Long userId = 1L;

        Mockito.doNothing().when(userService).deleteUser(userId);

        mockMvc.perform(delete("/api/user/{userId}", userId)
                .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
