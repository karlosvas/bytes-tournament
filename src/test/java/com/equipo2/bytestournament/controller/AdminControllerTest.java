package com.equipo2.bytestournament.controller;

import com.equipo2.bytestournament.DTO.UserDTO;
import com.equipo2.bytestournament.contoller.AdminController;
import com.equipo2.bytestournament.enums.AuthorityPrivilegies;
import com.equipo2.bytestournament.service.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

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
@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    /**
     * Prueba para el endpoint del dashboard de admin.
     * Simula una petición GET al endpoint "/api/admin/dashboard" y verifica que la respuesta sea OK (200).
     */
    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    void dashboard_returnsOk() throws Exception {
        // Simulamos una peticion get al dashboard de admin y vemosq ue funcione
        mockMvc.perform(get("/api/admin/dashboard").with(csrf()))
               .andExpect(status().isOk())
               .andExpect(content().string("Admin Dashboard"));
    }

    /**
     * Prueba para el endpoint de añadir autoridad a un usuario.
     * Simula una petición PUT al endpoint "/api/admin/createAdmin" con un usuario con rol ADMIN
     */
    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    void addAuthorityToUserTest() throws Exception {
        // Simulamos el comportamiento del servicio para añadir autoridad al usuario
        Mockito.when(userDetailsService.addAuthorityCreateAdmin("test", AuthorityPrivilegies.USER_CREATE))
                .thenReturn(new UserDTO());

        // Realizamos la petición PUT al endpoint para crear un administrador
        mockMvc.perform(put("/api/admin/createAdmin").with(csrf())
                        .param("username", "test"))
                .andExpect(status().isOk());
    }
}
