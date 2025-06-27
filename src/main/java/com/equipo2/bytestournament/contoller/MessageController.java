package com.equipo2.bytestournament.contoller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.equipo2.bytestournament.DTO.MessageDTO;
import com.equipo2.bytestournament.service.MessageService;

import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Message", description = "Controlador para la gestión de mensajes entre usuarios")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/tournaments/{tournamentId}/messages")
    @PreAuthorize("hasRole('PLAYER') or hasRole('ADMIN')")
    public List<MessageDTO> getTournamentMessages(@PathVariable Long tournamentId) {
        return messageService.getTournamentMessages(tournamentId);
    }

    @PostMapping("/tournaments/{tournamentId}/messages")
    @PreAuthorize("hasRole('PLAYER') or hasRole('ADMIN')")
    public void sendTournamentMessage(@PathVariable Long tournamentId, @RequestBody MessageDTO dto) {
        messageService.sendTournamentMessage(tournamentId, dto);
    }

    //Obtengo el mensaje de la partida
    @GetMapping("/matches/{matchId}/messages")
    @PreAuthorize("hasRole('PLAYER') or hasRole('ADMIN')")
    public List<MessageDTO> getMatchMessages(@PathVariable Long matchId) {
        return getMatchMessages(matchId);
    }

    //Enviar mensaje a partida
    @PostMapping("/matches/{matchId}/messages")
    @PreAuthorize("hasRole('PLAYER') or hasRole('ADMIN')")
    public ResponseEntity<Void> sendMatchMessage(@PathVariable Long matchId, @RequestBody MessageDTO dto) {
        return messageService.sendMatchMessage(matchId, dto);
    }
}
