package com.equipo2.bytestournament.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import com.equipo2.bytestournament.DTO.RankingDTO;
import com.equipo2.bytestournament.DTO.RankingDetailsDTO;
import com.equipo2.bytestournament.DTO.TournamentDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.equipo2.bytestournament.contoller.TournamentController;
import com.equipo2.bytestournament.service.TournamentService;
import com.equipo2.bytestournament.service.UserService;

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
@WebMvcTest(TournamentController.class)
public class TournamentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
     private TournamentService tournamentService;

    @MockBean
     private UserService userService;

    /**
     * Prueba para el endpoint de creación de torneos.
     * Simula una petición POST al endpoint "/api/tournaments" con un usuario con rol ADMIN.
     */
    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void createTest() throws Exception {
        String tournamentDTO = """
                {
                "name": "Torneo de test",
                "maxPlayers": 10,
                "status": "PENDING",
                "maxRounds": 10
                }
        """;

        Mockito.when(tournamentService.createTournament(Mockito.any(TournamentDTO.class)))
                .thenReturn(new TournamentDTO());

        mockMvc.perform(post("/api/tournaments").with(csrf())
                .contentType("application/json")
                .content(tournamentDTO))
                .andExpect(status().isCreated());
    }

    /**
     * Prueba para el endpoint de unión a un torneo.
     * Simula una petición POST al endpoint "/api/tournaments" con un usuario con rol ADMIN.
     */
    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void addPlayerToTournamentTest() throws Exception {
        Long tournamentId = 1L;
        String userName = "jugador1";

        Mockito.when(tournamentService.addPlayerToTournament(tournamentId, userName))
                .thenReturn(new TournamentDTO());

        mockMvc.perform(post("/api/tournaments/players")
                        .with(csrf())
                        .param("tournamentId", tournamentId.toString())
                        .param("userName", userName))
                .andExpect(status().isOk());
    }

    /**
     * Prueba para el endpoint de unión a un torneo.
     * Simula una petición POST al endpoint "/api/tournaments" con un usuario con rol ADMIN.
     */
    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void joinTournamentWithActualUserTest() throws Exception {
        Long tournamentId = 1L;
        String userName = "jugador1";

        Mockito.when(tournamentService.addPlayerToTournament(tournamentId, "test"))
                .thenReturn(new TournamentDTO());

        mockMvc.perform(post("/api/tournaments/players/me")
                        .with(csrf())
                        .param("tournamentId", tournamentId.toString())
                        .param("userName", userName))
                .andExpect(status().isCreated());
    }

    /**
     * Prueba para obtener la clasificación de un torneo.
     * Simula una petición GET al endpoint "/api/tournaments/ranking/{tournamentId}" con un usuario con rol ADMIN.
     * @param tournamentId ID del torneo para el cual se quiere obtener la clasificación.
     */
    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void getClassificationPrettyFalseTest() throws Exception {
        Long tournamentId = 1L;
        RankingDTO ranking = new RankingDTO();

        Mockito.when(tournamentService.getClassification(tournamentId))
                .thenReturn(ranking);

        mockMvc.perform(get("/api/tournaments/ranking/{tournamentId}", tournamentId)
                        .param("pretty", "false"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));

        mockMvc.perform(get("/api/tournaments/ranking/{tournamentId}", tournamentId)
                        .param("pretty", "true"))
                .andExpect(status().isOk())
                .andExpect(content().string(ranking.toString()));
    }

    /**
     * Prueba para obtener los detalles de la clasificación de un torneo.
     * Simula una petición GET al endpoint "/api/tournaments/ranking/details/{tournament
     * Id}" con un usuario con rol ADMIN.
     */
    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void getRankingDetailsTest() throws Exception {
        Long tournamentId = 1L;
        List<RankingDetailsDTO> ranking = List.of(new RankingDetailsDTO());

        Mockito.when(tournamentService.getRankingDetails(tournamentId))
                .thenReturn(ranking);

        mockMvc.perform(get("/api/tournaments/ranking/details/{tournamentId}", tournamentId)
                        .param("pretty", "false"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));

        mockMvc.perform(get("/api/tournaments/ranking/details/{tournamentId}", tournamentId)
                        .param("pretty", "true"))
                .andExpect(status().isOk())
                .andExpect(content().string(ranking.toString()));
    }

    /**
     * Prueba para actualizar un torneo.
     * Simula una petición PUT al endpoint "/api/tournaments" con un usuario con rol ADMIN.
     * El torneo se actualiza con los datos proporcionados en el cuerpo de la petición.
     */
    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void updateTournamentTest() throws Exception {
        String tournamentDTO = """
                {
                  "name": "Torneo Actualizado 2025",
                  "maxPlayers": 10,
                  "status": "PENDING",
                  "maxRounds": 10
                }
        """;

        Mockito.when(tournamentService.updateTournament(Mockito.any(TournamentDTO.class), Mockito.any()))
                .thenReturn(new TournamentDTO());

        mockMvc.perform(put("/api/tournaments").with(csrf())
                .contentType("application/json")
                .content(tournamentDTO))
                .andExpect(status().isOk());
    }
}
