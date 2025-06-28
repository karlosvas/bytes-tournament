package com.equipo2.bytestournament.service;

import com.equipo2.bytestournament.DTO.MessageDTO;
import com.equipo2.bytestournament.enums.Status;
import com.equipo2.bytestournament.exceptions.RequestException;
import com.equipo2.bytestournament.mapper.MessageMapper;
import com.equipo2.bytestournament.model.Match;
import com.equipo2.bytestournament.model.Message;
import com.equipo2.bytestournament.model.Tournament;
import com.equipo2.bytestournament.model.User;
import com.equipo2.bytestournament.repository.MatchRepository;
import com.equipo2.bytestournament.repository.MessageRepository;
import com.equipo2.bytestournament.repository.TournamentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;
    @Mock
    private MessageMapper messageMapper;
    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private MatchRepository matchRepository;

    @InjectMocks
    private MessageService messageService;

    private Message message;
    private MessageDTO messageDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        message = Message.builder()
                .id(1L)
                .content("Hola mundo")
                .senderId(10L)
                .build();

        messageDTO = MessageDTO.builder()
                .id(1L)
                .content("Hola mundo")
                .senderId(10L)
                .build();
    }

    /**
     * Test de obtener mensajes de un torneo
     * - Caso encontrado: se espera que retorne una lista de mensajes.
     * - Caso no encontrado: se espera que lance una RequestException.
     */
    @Test
    void testGetTournamentMessages() {
        Long tournamentId = 1L;

        Tournament tournament = Tournament.builder()
                .id(tournamentId)
                .name("Torneo de prueba")
                .maxPlayers(10)
                .status(Status.PENDING)
                .build();

        when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(tournament));
        List<Message> messages = List.of(message);
        List<MessageDTO> messageDTOs = List.of(messageDTO);

        when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(new Tournament()));
        when(messageRepository.findByTournamentIdOrderByTimestampAsc(1L)).thenReturn(messages);
        when(messageMapper.messageListToMessageDTOsList(messages)).thenReturn(messageDTOs);

        List<MessageDTO> result = messageService.getTournamentMessages(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Hola mundo", result.get(0).getContent());
        verify(messageRepository, times(1)).findByTournamentIdOrderByTimestampAsc(1L);
    }

    /**
     * Test de enviar mensajes
     * - Caso encontrado: se espera que retorne el mensaje enviado.
     * - Caso no encontrado: se espera que lance una RequestException.
     */
    @Test
    void testSaveMessage() {
        Long tournamentId = 1L;

        Tournament tournament = Tournament.builder()
                .id(tournamentId)
                .name("Torneo de prueba")
                .build();

        when(matchRepository.findById(1L)).thenReturn(Optional.of(new Match()));
        when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(tournament));
        when(messageMapper.messageDTOToMessage(any(MessageDTO.class))).thenReturn(message);
        when(messageRepository.save(any(Message.class))).thenReturn(message);
        when(messageMapper.messageToMessageDTO(any(Message.class))).thenReturn(messageDTO);

        MessageDTO result = messageService.sendMatchMessage(1L, messageDTO);

        assertNotNull(result);
        assertEquals(messageDTO.getContent(), result.getContent());
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    /**
     * Test que obtiene todos los mensajes
     */
    @Test
    void testGetAllMessages() {
        List<Message> messages = List.of(message);
        List<MessageDTO> messageDTOs = List.of(messageDTO);

        when(messageRepository.findAll()).thenReturn(messages);
        when(messageMapper.messageListToMessageDTOsList(messages)).thenReturn(messageDTOs);

        List<MessageDTO> result = messageService.getAllMessages();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(messageRepository, times(1)).findAll();
    }

    /**
     * Test que actualiza un mensaje
     * - Caso encontrado: se espera que retorne el mensaje actualizado.
     * - Caso no encontrado: se espera que lance una RequestException.
     */
    @Test
    void deleteMessageTest() {
        when(messageRepository.findById(1L)).thenReturn(Optional.of(new Message()));
        doNothing().when(messageRepository).deleteById(1L);

        messageService.deleteMessage(1L);

        verify(messageRepository, times(1)).deleteById(1L);
    }

    /**
     * Ejemplo de caso de error al borrar un mensaje
     * - Caso no encontrado: se espera que lance una RequestException.
     * - Caso encontrado: se espera que no se llame al método deleteById
     */
    @Test
    void deleteMessageNotFound() {
        when(messageRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RequestException.class, () -> messageService.deleteMessage(2L));
        verify(messageRepository, never()).deleteById(2L);
    }
}