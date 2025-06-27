package com.equipo2.bytestournament.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.equipo2.bytestournament.contoller.MessageController;
import com.equipo2.bytestournament.service.MessageService;
import com.equipo2.bytestournament.DTO.MessageDTO;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

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
@WebMvcTest(MessageController.class)
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
     private MessageService messageService;

    /**
     * Prueba para el endpoint de generación de partidos.
     * Simula una petición POST al endpoint "/api/matches/generate/{tournamentId}" con un usuario con rol ADMIN.
     */
    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void generateMatchesTest() throws Exception {
        Mockito.when(messageService.getTournamentMessages(1L)).thenReturn(List.of(new MessageDTO()));
        String content = """
            {
                "senderId": 10,
                "content": "¡Hola!",
                "matchId": 300,
                "tournamentId": 200
            }
        """;

       mockMvc.perform(post("/api/message/tournament/{tournamentId}", 1L)
        .with(csrf())
        .contentType("application/json")
        .content(content))
        .andExpect(status().isCreated());
    }

    /**
     * Prueba para el envío de mensajes a un torneo.
     * Simula una petición POST al endpoint "/api/message/tournament/{tournamentId}"
     */
    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void sendTournamentMessageTest() throws Exception {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setContent("Test message");
        Mockito.when(messageService.sendTournamentMessage(Mockito.anyLong(), Mockito.any(MessageDTO.class)))
                .thenReturn(messageDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/message/tournament/{tournamentId}", 1L)
                .with(csrf())
                .contentType("application/json")
                .content("{\"content\":\"Test message\"}");

        mockMvc.perform(request)
                .andExpect(status().isCreated());
    }

    /**
     * Prueba para obtener los mensajes de un torneo.
     * Simula una petición GET al endpoint "/api/message/tournament/{tournamentId}"
     */
    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void sendMatchMessageTest() throws Exception {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setContent("Test match message");
        Mockito.when(messageService.sendMatchMessage(Mockito.anyLong(), Mockito.any(MessageDTO.class)))
                .thenReturn(messageDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/message/match/{matchId}", 1L)
                .with(csrf())
                .contentType("application/json")
                .content("{\"content\":\"Test match message\"}");

        mockMvc.perform(request)
                .andExpect(status().isCreated());
    }

    /**
     * Prueba para obtener los mensajes de una partida.
     * Simula una petición GET al endpoint "/api/message/match/{matchId}"
     */
    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void getAllMessagesTest() throws Exception {

    }

    /**
     * Prueba para actualizar un mensaje.
     * Simula una petición PUT al endpoint "/api/message/{id}".
     */
    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void updateMessageTest() throws Exception {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setContent("Updated message");
        Mockito.when(messageService.updateMessage(Mockito.anyLong(), Mockito.any(MessageDTO.class)))
                .thenReturn(messageDTO);

        String content = """
             {
                "senderId": 10,
                "content": "Update message",
                "matchId": 300,
                "tournamentId": 200
            }
        """;

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/api/message/{id}", 1L)
                .with(csrf())
                .contentType("application/json")
                .content(content);

        mockMvc.perform(request)
                .andExpect(status().isOk());
    }   

    /**
     * Prueba para eliminar un mensaje.
     * Simula una petición DELETE al endpoint "/api/message/{id}".
     */
    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void deleteMessageTest() throws Exception {
        Mockito.doNothing().when(messageService).deleteMessage(Mockito.anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/message/{id}", 1L).with(csrf()))
                .andExpect(status().isNoContent());
    }
}