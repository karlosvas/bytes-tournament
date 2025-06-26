package com.equipo2.bytestournament.mapper;

import com.equipo2.bytestournament.DTO.MessageDTO;
import com.equipo2.bytestournament.model.Message;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class MessageMapperTest {

    private static final MessageMapper messageMapper = Mappers.getMapper(MessageMapper.class);

    @Test
    void testMessageToMessageDTO() {
        Message message = Message.builder()
                .id(1L)
                .senderId(10L)
                .content("¡Hola!")
                .timestamp(LocalDateTime.of(2024, 6, 16, 15, 30))
                .matchId(100L)
                .tournamentId(200L)
                .build();

        MessageDTO dto = messageMapper.messageToMessageDTO(message);

        assertNotNull(dto);
        assertEquals(message.getId(), dto.getId());
        assertEquals(message.getSenderId(), dto.getSenderId());
        assertEquals(message.getContent(), dto.getContent());
        assertEquals(message.getTimestamp(), dto.getTimestamp());
        assertEquals(message.getMatchId(), dto.getMatchId());
        assertEquals(message.getTournamentId(), dto.getTournamentId());
    }

    @Test
    void testMessageDTOToMessage() {
        MessageDTO dto = MessageDTO.builder()
                .id(2L)
                .senderId(20L)
                .content("¡Adiós!")
                .timestamp(LocalDateTime.of(2024, 6, 17, 10, 0))
                .matchId(101L)
                .tournamentId(201L)
                .build();

        Message message = messageMapper.messageDtoToMessage(dto);

        assertNotNull(message);
        assertEquals(dto.getId(), message.getId());
        assertEquals(dto.getSenderId(), message.getSenderId());
        assertEquals(dto.getContent(), message.getContent());
        assertEquals(dto.getTimestamp(), message.getTimestamp());
        assertEquals(dto.getMatchId(), message.getMatchId());
        assertEquals(dto.getTournamentId(), message.getTournamentId());
    }

    @Test
    void testMessageListToMessageDTOList() {
        Message message = Message.builder()
                .id(3L)
                .senderId(30L)
                .content("Mensaje de prueba")
                .timestamp(LocalDateTime.now())
                .matchId(102L)
                .tournamentId(202L)
                .build();

        List<MessageDTO> dtoList = messageMapper.messageToMessageDtos(List.of(message));
        assertNotNull(dtoList);
        assertEquals(1, dtoList.size());
        assertEquals(message.getId(), dtoList.get(0).getId());
    }

    @Test
    void testMessageDTOListToMessageList() {
        MessageDTO dto = MessageDTO.builder()
                .id(4L)
                .senderId(40L)
                .content("Otro mensaje")
                .timestamp(LocalDateTime.now())
                .matchId(103L)
                .tournamentId(203L)
                .build();

        List<Message> messageList = messageMapper.messageDtosToMessages(List.of(dto));
        assertNotNull(messageList);
        assertEquals(1, messageList.size());
        assertEquals(dto.getId(), messageList.get(0).getId());
    }
}