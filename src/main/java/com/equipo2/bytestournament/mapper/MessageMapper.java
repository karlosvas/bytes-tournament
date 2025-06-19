package com.equipo2.bytestournament.mapper;

import com.equipo2.bytestournament.DTO.MessageDTO;
import com.equipo2.bytestournament.model.Message;
import com.equipo2.bytestournament.repository.MessageRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.ArrayList;

/**
 * Mapper que convierte la entidad Message a DTO y la DTO a entidad
 *
 * @author Christian Escalas
 */
@Component
public class MessageMapper {

    private final MessageRepository messageRepository;

    /**
     * Constructor para inyectar las dependencias.
     *
     * @param userRepository repositorio de usuarios.
     */
    public MessageMapper(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    /**
     * Convierte un MessageDTO en un Message.
     * <p>
     * Si messageDTO es null, devuelve null.
     *
     * @param messageDTO con los datos del mensaje.
     * @return una entidad Message con los campos asignados desde el DTO.
     */
    public Message messageDtoToMessage(MessageDTO messageDTO) {

        if (messageDTO == null) {
            return null;
        }

        Message message = new Message();

        message.setContent(messageDTO.getContent());

        return message;
    }

    /**
     * Convierte Message en un MessageDTO.
     * <p>
     * Si message es null, devuelve null.
     *
     * @param message entidad que hay que convertir a DTO.
     * @return DTO con los mismos valores que la entidad.
     */
    public MessageDTO messageToMessageDTO(Message message) {

        if (message == null) {
            return null;
        }

        MessageDTO messageDTO = new MessageDTO();

        messageDTO.setId(message.getId());
        messageDTO.setSenderId(message.getSenderId());
        messageDTO.setContent(message.getContent());
        messageDTO.setTimestamp(message.getTimestamp());
        messageDTO.setMatchId(message.getMatchId());
        messageDTO.setTournamentId(message.getTournamentId());

        return messageDTO;
    }

    /**
     * Convierte una lista de entidades Message a una lista de MessageDTO.
     * <p>
     * Si messages es null, devuelve una lista vacía.
     * Cada elemento message de la lista se convierte a MessageDTO.
     *
     * @param messages lista de entidades Message. Puede ser null.
     * @return lista de DTO, pero si la lista messages está vacía devuelve una lista vacía.
     */
    public List<MessageDTO> messageToMessageDtos(List<Message> messages) {

        if (messages == null) {
            return List.of();
        }

        List<MessageDTO> messagesDtos = new ArrayList<>();
        for (Message message : messages) {
            messagesDtos.add(messageToMessageDTO(message));
        }
        return messagesDtos;
    }

    /**
     * Actualiza los campos de la entidad Message.
     * <p>
     * Si messageDTO es null o message es null, no hace nada.
     *
     * @param message    entidad que se va a actualizar. Si es null, no hace nada.
     * @param messageDTO con los nuevos valores. Si es null, no hace nada.
     */
    public void updateMessage(Message message, MessageDTO messageDTO) {

        if (message == null || messageDTO == null) {
            return;
        } else {
            message.setContent(messageDTO.getContent());
        }
    }
}