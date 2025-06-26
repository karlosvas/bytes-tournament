package com.equipo2.bytestournament.contoller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.equipo2.bytestournament.DTO.MessageDTO;
import com.equipo2.bytestournament.mapper.MessageMapper;
import com.equipo2.bytestournament.model.Message;
import com.equipo2.bytestournament.repository.MessageRepository;
import com.equipo2.bytestournament.repository.UserRepository;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Message", description = "Controlador para la gestión de mensajes entre usuarios")
public class MessageController {
 private final MessageRepository messageRepository;
    private final UserRepository userRepository ;
    private final MessageMapper messageMapper;

    @GetMapping("/tournaments/{tournamentId}/messages")
    @PreAuthorize("hasRole('PLAYER') or hasRole('ADMIN')")
    public ResponseEntity<List<MessageDTO>> getTournamentMessages(@PathVariable Long tournamentId) {
        List<Message> messages = messageRepository.findByTournamentIdOrderByTimestampAsc(tournamentId);
        return ResponseEntity.ok(messageMapper.messageToMessageDtos(messages));
    }

     @PostMapping("/tournaments/{tournamentId}/messages")
    @PreAuthorize("hasRole('PLAYER') or hasRole('ADMIN')")
    public ResponseEntity<Void> sendTournamentMessage(@PathVariable Long tournamentId, @RequestBody MessageDTO dto) {
        if (dto.getSenderId() == null || dto.getContent() == null) {
            return ResponseEntity.badRequest().build();
        }

        Message message = messageMapper.messageDtoToMessage(dto);
        message.setTournamentId(tournamentId);
        message.setMatchId(null);
        message.setTimestamp(LocalDateTime.now());

        messageRepository.save(message);
        return ResponseEntity.ok().build();
    }

    //Obtengo el mensaje de la partida
    
    @GetMapping("/matches/{matchId}/messages")
    @PreAuthorize("hasRole('PLAYER') or hasRole('ADMIN')")
    public ResponseEntity<List<MessageDTO>> getMatchMessages(@PathVariable Long matchId) {
        List<Message> messages = messageRepository.findByMatchIdOrderByTimestampAsc(matchId);
        return ResponseEntity.ok(messageMapper.messageToMessageDtos(messages));
    }

    //Enviar mensaje a partida

@PostMapping("/matches/{matchId}/messages")
    @PreAuthorize("hasRole('PLAYER') or hasRole('ADMIN')")
    public ResponseEntity<Void> sendMatchMessage(@PathVariable Long matchId, @RequestBody MessageDTO dto) {
        if (dto.getSenderId() == null || dto.getContent() == null) {
            return ResponseEntity.badRequest().build();
        }

        Message message = messageMapper.messageDtoToMessage(dto);
        message.setMatchId(matchId);
        message.setTournamentId(null);
        message.setTimestamp(LocalDateTime.now());

        messageRepository.save(message);
        return ResponseEntity.ok().build();
    }
}
