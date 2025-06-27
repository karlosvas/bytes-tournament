package com.equipo2.bytestournament.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.equipo2.bytestournament.DTO.MatchDTO;
import com.equipo2.bytestournament.enums.ApiResponse;
import com.equipo2.bytestournament.exceptions.RequestException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.equipo2.bytestournament.contoller.MatchController;
import com.equipo2.bytestournament.service.MatchService;
import java.util.List;

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
@WebMvcTest(MatchController.class)
public class MatchControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
     private MatchService matchService;

    /**
     * Prueba para el endpoint de generación de partidos.
     * Simula una petición POST al endpoint "/api/matches/generate/{tournamentId}" con un usuario con rol ADMIN.
     */
    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void generateMatchesTest() throws Exception {
        Mockito.when(matchService.generateMatches(1L)).thenReturn(List.of(new MatchDTO()));

        mockMvc.perform(post("/api/matches/generate/{tournamentId}", 1L).with(csrf()))
                .andExpect(status().isCreated());
    }

    /**
     * Prueba para el endpoint de verificación de un partido.
     * Simula una petición GET al endpoint "/api/matches/{matchId}" con un usuario con rol ADMIN.
     */
    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    void checkMatchTest() throws Exception {
        Mockito.when(matchService.checkMatch(1L)).thenReturn(new MatchDTO());

        mockMvc.perform(get("/api/matches/{matchId}", 1L).with(csrf()))
                .andExpect(status().isOk());
    }

    /**
     * Prueba para el endpoint de verificación de un partido que no existe.
     * Simula una petición GET al endpoint "/api/matches/{matchId}" con un usuario con rol ADMIN
     */
    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    void checkMatchTestNotFound() throws Exception {
        Mockito.when(matchService.checkMatch(99L)).thenThrow(new RequestException(ApiResponse.NOT_FOUND));

        mockMvc.perform(get("/api/matches/{matchId}", 99L).with(csrf()))
                .andExpect(status().isNotFound());
    }

    /**
     * Prueba para el endpoint de actualización del resultado de un partido.
     * Simula una petición PUT al endpoint "/api/matches/{matchId}/result" con un usuario con rol ADMIN.
     */
    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void updateMatchResultTest() throws Exception {
        Long matchId = 1L;
        String macthDTO = """
                {
                    "id": 1,
                    "tournament": 1,
                    "player1": 1,
                    "player2": 2,
                    "result": "PLAYER1_WIN",
                    "round": 5
                }
                """;

        Mockito.when(matchService.updateMatchResult(matchId, new MatchDTO())).thenReturn(new MatchDTO());

        mockMvc.perform(put("/api/matches/{matchId}/result", matchId)
                .with(csrf())
                .contentType("application/json")
                .content(macthDTO))
                .andExpect(status().isOk());
    }

    /**
     * Prueba para el endpoint de actualización del resultado de un partido que no existe.
     * Simula una petición PUT al endpoint "/api/matches/{matchId}/result"
     */
    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void updateMatchResultTestNotFound() throws Exception {
        Long matchId = 99L;
        String macthDTO = """
                {
                    "tournament": 100,
                    "player1": 34,
                    "player2": 35,
                    "result": "PLAYER2_WIN",
                    "round": 1
                }
                """;

        Mockito.when(matchService.updateMatchResult(matchId, new MatchDTO()))
                .thenThrow(new RequestException(ApiResponse.NOT_FOUND));

        mockMvc.perform(put("/api/matches/{matchId}/result", matchId)
                .with(csrf())
                .contentType("application/json")
                .content(macthDTO))
                .andExpect(status().isOk());
    }

    /**
     * Prueba para eliminar un partido.
     * Simula una petición DELETE al endpoint "/api/matches/{matchId}" con un usuario con rol ADMIN.
     */
    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void deleteMatchTest() throws Exception {
        Long matchId = 1L;

        Mockito.doNothing().when(matchService).deleteMatch(matchId);

        mockMvc.perform(delete("/api/matches/{matchId}", matchId)
                .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
