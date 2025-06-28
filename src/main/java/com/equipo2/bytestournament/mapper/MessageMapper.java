package com.equipo2.bytestournament.mapper;

import com.equipo2.bytestournament.DTO.MessageDTO;
import com.equipo2.bytestournament.model.Message;
import org.mapstruct.Mapper;
import java.util.List;

/**
 * Mapper que convierte la entidad Message a DTO y la DTO a entidad
 * y viceversa.
 * Utiliza MapStruct para generar el código de mapeo automáticamente.
 * 
 * {@link Mapper} Anotación de MapStruct que indica que esta interfaz es un mapper.
 * {@link Mapping} Anotación de MapStruct que indica cómo se deben mapear
 */

 
@Mapper(componentModel = "spring")
public interface MessageMapper {
    // Message -> MessageDTO
    MessageDTO messageToMessageDTO(Message message);

    // MessageDTO -> Message
    Message messageDTOToMessage(MessageDTO messageDTO);

    // List<Message> -> List<MessageDTO>
    List<MessageDTO> messageListToMessageDTOsList(List<Message> messages);

    // List<MessageDTO> -> List<Message>
    List<Message> messageDTOsListToMessagesList(List<MessageDTO> messageDTOS);
}