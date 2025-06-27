package com.equipo2.bytestournament.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.equipo2.bytestournament.DTO.MessageDTO;
import com.equipo2.bytestournament.mapper.MessageMapper;
import com.equipo2.bytestournament.model.Message;
import com.equipo2.bytestournament.repository.MessageRepository;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    public MessageService(MessageRepository messageRepository, MessageMapper messageMapper) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
    }

    public List<MessageDTO> getTournamentMessages(Long tournamentId) {
         List<Message> messages = messageRepository.findByTournamentIdOrderByTimestampAsc(tournamentId);
        return messageMapper.messageToMessageDtos(messages);
    }

    public void sendTournamentMessage(Long tournamentId, MessageDTO dto) {
         Message message = messageMapper.messageDtoToMessage(dto);
        message.setTournamentId(tournamentId);
        message.setMatchId(null);
        message.setTimestamp(LocalDateTime.now());

        messageRepository.save(message);
    }

    public List<MessageDTO> getMatchMessages(Long matchId) {
        List<Message> messages = messageRepository.findByMatchIdOrderByTimestampAsc(matchId);
        return messageMapper.messageToMessageDtos(messages);
    }

    public ResponseEntity<Void> sendMatchMessage(Long matchId, MessageDTO dto) {
       Message message = messageMapper.messageDtoToMessage(dto);
        message.setMatchId(matchId);
        message.setTournamentId(null);
        message.setTimestamp(LocalDateTime.now());

        messageRepository.save(message);
        return ResponseEntity.ok().build();
    }
}
