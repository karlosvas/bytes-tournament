package com.equipo2.bytestournament.contoller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.equipo2.bytestournament.DTO.MessageDTO;
import com.equipo2.bytestournament.annotations.SwaggerApiResponses;
import com.equipo2.bytestournament.enums.ApiResponse;
import com.equipo2.bytestournament.service.MessageService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/message")
@Tag(name = "Message", description = "Controlador para la gestión de mensajes entre usuarios")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    
    /**
     * Obtiene todos los mensajes asociados a un torneo específico.
     * Este método es accesible para usuarios con rol PLAYER o ADMIN.
     *
     * @param tournamentId ID del torneo.
     * @return Lista de MessageDTO con los mensajes del torneo.
     */
    @SwaggerApiResponses
    @GetMapping("/tournament/{tournamentId}")
    @PreAuthorize("hasAnyRole('PLAYER','ADMIN')")
    @Operation(summary = "Obtener mensajes de un torneo", description = "Obtiene todos los mensajes asociados a un torneo específico.")
    public List<MessageDTO> getTournamentMessages(@PathVariable Long tournamentId) {
        return messageService.getTournamentMessages(tournamentId);
    }

    /**
     * Envía un mensaje a un torneo específico.
     * Este método es accesible para usuarios con rol PLAYER o ADMIN.
     *
     * @param tournamentId ID del torneo.
     * @param dto          Datos del mensaje a enviar.
     * @return El mensaje creado (MessageDTO).
     */
    @SwaggerApiResponses
    @PostMapping("/tournament/{tournamentId}")
    @PreAuthorize("hasAnyRole('PLAYER','ADMIN')")
    @Operation(summary = "Enviar mensaje a un torneo", description = "Envía un mensaje a un torneo específico.")
    public ResponseEntity<MessageDTO> sendTournamentMessage(@PathVariable Long tournamentId, @RequestBody MessageDTO dto) {
        MessageDTO newMessageDTO = messageService.sendTournamentMessage(tournamentId, dto);
        return ResponseEntity.status(ApiResponse.CREATED.getStatus()).body(newMessageDTO);
    }

    /**
     * Obtiene todos los mensajes asociados a una partida específica.
     * Este método es accesible para usuarios con rol PLAYER o ADMIN.
     *
     * @param matchId ID de la partida.
     * @return Lista de MessageDTO con los mensajes de la partida.
     */
    @SwaggerApiResponses    
    @GetMapping("/match/{matchId}")
    @PreAuthorize("hasAnyRole('PLAYER','ADMIN')")
    @Operation(summary = "Obtener mensajes de una partida", description = "Obtiene todos los mensajes asociados a una partida específica.")
    public List<MessageDTO> getMatchMessages(@PathVariable Long matchId) {
        return messageService.getMatchMessages(matchId);
    }

   /**
     * Envía un mensaje a una partida específica.
     * Este método es accesible para usuarios con rol PLAYER o ADMIN.
     *
     * @param matchId ID de la partida.
     * @param dto     Datos del mensaje a enviar.
     * @return El mensaje creado (MessageDTO).
     */
    @SwaggerApiResponses
    @PostMapping("/match/{matchId}")
    @PreAuthorize("hasAnyRole('PLAYER','ADMIN')")
    public ResponseEntity<MessageDTO> sendMatchMessage(@PathVariable Long matchId, @RequestBody MessageDTO dto) {
        return ResponseEntity.status(ApiResponse.CREATED.getStatus()).body(messageService.sendMatchMessage(matchId, dto));
    }

     /**
     * Obtiene una lista de todos los mensajes enviados en el sistema.
     * Este método es accesible para usuarios con rol PLAYER o ADMIN.
     *
     * @return Lista de MessageDTO con todos los mensajes.
     */
    @SwaggerApiResponses
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('PLAYER','ADMIN')")
    @Operation(summary = "Obtener todos los mensajes", description = "Obtiene una lista de todos los mensajes enviados en el sistema.")
    public List<MessageDTO> getAllMessages() {
        return messageService.getAllMessages();
    }

    /**
     * Actualiza un mensaje existente por su ID.
     * Este método es accesible solo para usuarios con el rol de ADMIN.
     *
     * @param id         ID del mensaje a actualizar.
     * @param messageDTO Datos actualizados del mensaje.
     * @return El mensaje actualizado (MessageDTO).
     */
    @SwaggerApiResponses
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar mensaje", description = "Actualiza un mensaje existente por su ID.")
    public MessageDTO updateMessage(@PathVariable Long id, @RequestBody @Valid MessageDTO messageDTO) {
        return messageService.updateMessage(id, messageDTO);
    }

    /**
     * Elimina un mensaje por su ID.
     * Este método es accesible solo para usuarios con el rol de ADMIN.
     * 
     * @param id ID del mensaje que se desea eliminar.
     * @return ResponseEntity<Void> con un estado HTTP 204 No Content si la eliminación fue exitosa.
     */
    @SwaggerApiResponses
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar mensaje", description = "Elimina un mensaje por su ID.")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}
