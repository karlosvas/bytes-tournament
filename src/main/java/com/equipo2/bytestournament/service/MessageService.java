package com.equipo2.bytestournament.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.equipo2.bytestournament.DTO.MessageDTO;
import com.equipo2.bytestournament.enums.ApiResponse;
import com.equipo2.bytestournament.exceptions.RequestException;
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

    /**
    * Obtiene los mensajes de un torneo específico.
     * @param tournamentId ID del torneo
     * @return Lista de mensajes del torneo
     */
    public List<MessageDTO> getTournamentMessages(Long tournamentId) {
        List<Message> messagesList = messageRepository.findByTournamentIdOrderByTimestampAsc(tournamentId);

        if(messagesList.isEmpty()) 
            throw new RequestException(ApiResponse.NOT_FOUND, "No se encontraron mensajes para el torneo con ID: " + tournamentId, "No se encontraron mensajes asociados al torneo con ID: " + tournamentId);

         Optional<Tournament> tournament = tournamentRepository.findById(tournamentId);

        if (!tournament.isPresent())
            throw new RequestException(ApiResponse.NOT_FOUND, "Torneo no encontrado", "No se pudo enviar el mensaje porque no se encontró el torneo con el ID proporcionado");

        return messageMapper.messageListToMessageDTOsList(messagesList);
    }

    /**
     * Envía un mensaje a un torneo específico.
     * * Verifica si el torneo existe antes de enviar el mensaje.
     * 
     * @param tournamentId ID del torneo al que se enviará el mensaje
     * @param dto DTO del mensaje a enviar
     * @return DTO del mensaje enviado
     */
    public MessageDTO sendTournamentMessage(Long tournamentId, MessageDTO dto) {
        Optional<Tournament> tournament = tournamentRepository.findById(tournamentId);

        if (tournament.isEmpty())
            throw new RequestException(ApiResponse.NOT_FOUND, "Torneo no encontrado", "No se pudo enviar el mensaje porque no se encontró el torneo con el ID proporcionado");

        Message message = messageMapper.messageDTOToMessage(dto);
        message.setTournamentId(tournamentId);
        message.setTimestamp(LocalDateTime.now());

        Message newMessage = messageRepository.save(message);
        return messageMapper.messageToMessageDTO(newMessage);
    }

    /**
     * Obtiene los mensajes de una partida específica.
     * Verifica si la partida existe antes de obtener los mensajes.
     * 
     * @param matchId ID de la partida
     * @return Lista de mensajes de la partida
     */
    public List<MessageDTO> getMatchMessages(Long matchId) {
        Optional<Match> match = matchRepository.findById(matchId);

        if (match.isEmpty()) 
            throw new RequestException(ApiResponse.NOT_FOUND, "Partida no encontrada", "No se encontró la partida con el ID proporcionado");
        
        
        List<Message> messages = messageRepository.findByMatchIdOrderByTimestampAsc(matchId);
        return messageMapper.messageListToMessageDTOsList(messages);
    }

    /**
     * Envía un mensaje a una partida específica.
     * Verifica si la partida existe antes de enviar el mensaje.
     * 
     * @param matchId ID de la partida a la que se enviará el mensaje
     * @param dto DTO del mensaje a enviar
     * @return DTO del mensaje enviado
     */
    public MessageDTO sendMatchMessage(Long matchId, MessageDTO dto) {
        Optional<Match> match = matchRepository.findById(matchId);

        if (match.isEmpty()) 
            throw new RequestException(ApiResponse.NOT_FOUND, "Partida no encontrada", "No se encontró la partida con el ID proporcionado");
        
        // Si existe 
        Message message = messageMapper.messageDTOToMessage(dto);
        message.setMatchId(matchId);
        message.setTimestamp(LocalDateTime.now());

        Message messageUpdated = messageRepository.save(message);
        return messageMapper.messageToMessageDTO(messageUpdated);
    }

    /**
     *  Obtiene todos los mensajes del sistema.
     *  Este método no requiere ningún parámetro y devuelve una lista de todos los mensajes.
     * 
     * @return Lista de todos los mensajes en formato DTO
     */
    public List<MessageDTO> getAllMessages() {
        List<Message> messages = messageRepository.findAll();
        return messageMapper.messageListToMessageDTOsList(messages);
    }
    
    /**
     * Actualiza un mensaje existente.
     * Verifica si el mensaje existe antes de intentar actualizarlo.
     * 
     * @param matchID ID del mensaje a actualizar
     * @param dto DTO del mensaje con los nuevos datos
     * @return DTO del mensaje actualizado
     */
    public MessageDTO updateMessage(Long matchID, MessageDTO dto) {
        Optional<Message> message = messageRepository.findById(matchID);
        if (!message.isPresent()) 
            throw new RequestException(ApiResponse.NOT_FOUND, "Mensaje no encontrado", "No se pudo actualizar el mensaje porque no se encontró con el ID proporcionado");
        
        Message newMessage = messageMapper.messageDTOToMessage(dto);
        newMessage.setId(matchID);
        newMessage.setTimestamp(LocalDateTime.now());

        Message updatedMessage = messageRepository.save(newMessage);
        return messageMapper.messageToMessageDTO(updatedMessage);
    }

    /**
     * Elimina un mensaje por su ID.
     * Verifica si el mensaje existe antes de intentar eliminarlo.
     * 
     * @param messageId
     */
    public void deleteMessage(Long messageId) {
       Optional<Message> message = messageRepository.findById(messageId);

       if(!message.isPresent())
            throw new RequestException(ApiResponse.NOT_FOUND, "Mensaje no encontrado", "No se pudo porque no se econtró mensaje con el ID proporcionado");
    
        messageRepository.deleteById(messageId);
    }
}
