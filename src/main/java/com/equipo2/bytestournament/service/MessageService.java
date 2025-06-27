package com.equipo2.bytestournament.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.equipo2.bytestournament.DTO.MessageDTO;
import com.equipo2.bytestournament.enums.ApiResponse;
import com.equipo2.bytestournament.exceptions.RequestException;
import com.equipo2.bytestournament.mapper.MatchMapper;
import com.equipo2.bytestournament.mapper.MessageMapper;
import com.equipo2.bytestournament.model.Match;
import com.equipo2.bytestournament.model.Message;
import com.equipo2.bytestournament.model.Tournament;
import com.equipo2.bytestournament.repository.MatchRepository;
import com.equipo2.bytestournament.repository.MessageRepository;
import com.equipo2.bytestournament.repository.TournamentRepository;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final TournamentRepository tournamentRepository;
    private final MatchRepository matchRepository;

    public MessageService(MessageRepository messageRepository, MessageMapper messageMapper, TournamentRepository tournamentRepository, MatchRepository matchRepository) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
        this.tournamentRepository = tournamentRepository;
        this.matchRepository = matchRepository;
    }

    public List<MessageDTO> getTournamentMessages(Long tournamentId) {
        List<Message> messages = messageRepository.findByTournamentIdOrderByTimestampAsc(tournamentId);

        if(messages.isEmpty()) 
            throw new RequestException(ApiResponse.NOT_FOUND, "No se encontraron mensajes para el torneo con ID: " + tournamentId, "No se encontraron mensajes asociados al torneo con ID: " + tournamentId);

         Optional<Tournament> tournament = tournamentRepository.findById(tournamentId);

        if (!tournament.isPresent())
            throw new RequestException(ApiResponse.NOT_FOUND, "Torneo no encontrado", "No se pudo enviar el mensaje porque no se encontró el torneo con el ID proporcionado");

        List<Message> messagesList = messageRepository.findByTournamentIdOrderByTimestampAsc(tournamentId);
        return messageMapper.messageToMessageDtos(messagesList);
    }

    public MessageDTO sendTournamentMessage(Long tournamentId, MessageDTO dto) {
        Optional<Tournament> tournament = tournamentRepository.findById(tournamentId);

        if (!tournament.isPresent())
            throw new RequestException(ApiResponse.NOT_FOUND, "Torneo no encontrado", "No se pudo enviar el mensaje porque no se encontró el torneo con el ID proporcionado");

        Message message = messageMapper.messageDtoToMessage(dto);
        message.setTournamentId(tournamentId);
        message.setTimestamp(LocalDateTime.now());

        Message newMessage = messageRepository.save(message);
        return messageMapper.messageToMessageDTO(newMessage);
    }

    public List<MessageDTO> getMatchMessages(Long matchId) {
        Optional<Match> match = matchRepository.findById(matchId);

        if (match.isEmpty()) 
            throw new RequestException(ApiResponse.NOT_FOUND, "Partida no encontrada", "No se encontró la partida con el ID proporcionado");
        
        
        List<Message> messages = messageRepository.findByMatchIdOrderByTimestampAsc(matchId);
        return messageMapper.messageToMessageDtos(messages);
    }

    public MessageDTO sendMatchMessage(Long matchId, MessageDTO dto) {
        Optional<Match> match = matchRepository.findById(matchId);

        if (match.isEmpty()) 
            throw new RequestException(ApiResponse.NOT_FOUND, "Partida no encontrada", "No se encontró la partida con el ID proporcionado");
        
        // Si existe 
        Message message = messageMapper.messageDtoToMessage(dto);
        message.setMatchId(matchId);
        message.setTimestamp(LocalDateTime.now());

        Message messageUpdated = messageRepository.save(message);
        return messageMapper.messageToMessageDTO(messageUpdated);
    }

    public List<MessageDTO> getAllMessages() {
        List<Message> messages = messageRepository.findAll();
        return messageMapper.messageToMessageDtos(messages);
    }
    
    public MessageDTO updateMessage(Long matchID, MessageDTO dto) {
        Optional<Message> message = messageRepository.findById(matchID);
        if (!message.isPresent()) 
            throw new RequestException(ApiResponse.NOT_FOUND, "Mensaje no encontrado", "No se pudo actualizar el mensaje porque no se encontró con el ID proporcionado");
        
        Message newMessage = messageMapper.messageDtoToMessage(dto);
        newMessage.setId(matchID);
        newMessage.setTimestamp(LocalDateTime.now());

        Message updatedMessage = messageRepository.save(newMessage);
        return messageMapper.messageToMessageDTO(updatedMessage);
    }

    public void deleteMessage(Long messageId) {
       Optional<Message> message = messageRepository.findById(messageId);

       if(!message.isPresent())
            throw new RequestException(ApiResponse.NOT_FOUND, "Mensaje no encontrado", "No se pudo porque no se econtró mensaje con el ID proporcionado");
    
        messageRepository.deleteById(messageId);
    }
}
