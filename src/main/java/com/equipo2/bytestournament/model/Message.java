package com.equipo2.bytestournament.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entidad JPA que representa un mensaje en el chat de una partida de la aplicación de torneos.
 * Contiene el id del mensaje, el id del jugador que lo envía, el contenido, la fecha y hora de envío del mensaje, el id de la partidaa y el id del torneo.
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "messages")
public class Message {
    /**
     * id: Identificador único del mensaje. No debe ser nulo
     * senderId: Identificador único del jugador que envía el mensaje. No debe ser nulo
     * conmtent: Contenido del mensaje enviado. No puede ser nulo.
     * timestamp: Fecha y hora de envío del mensaje. No puede ser nulo.
     * Identificador único del torneo. No debe ser nulo
     * tournamentId: Identificador único del torneo que se genera automaticamente
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "sender_id", updatable = false, nullable = false)
    private Long senderId;

    @Column(name = "content", updatable = true, nullable = false)
    private String content;

    @Column(name = "timestamp", updatable = false, nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "match_id", updatable = false, nullable = false)
    private Long matchId;

    @Column(name = "tournament_id", updatable = false, nullable = false)
    private Long tournamentId;
}