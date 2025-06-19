package com.equipo2.bytestournament.mapper;

import com.equipo2.bytestournament.DTO.MessageDTO;
import com.equipo2.bytestournament.model.Message;
import org.mapstruct.Mapper;
import java.util.List;

/**
 * Mapper que convierte la entidad Message a DTO y la DTO a entidad
 *
 * @author Christian Escalas
 */
@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageDTO messageToMessageDTO(Message message);

    Message messageDtoToMessage(MessageDTO messageDTO);

    List<MessageDTO> messageToMessageDtos(List<Message> messages);

    List<Message> messageDtosToMessages(List<MessageDTO> messageDTOS);
}