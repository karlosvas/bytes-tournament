package com.equipo2.bytestournament.model;

import jakarta.persistence.*;
import lombok.Data;
import com.equipo2.bytestournament.enums.Result;

/**
 * Entidad JPA que representa una partida de un torneo de la aplicación de torneos.
 * Contiene el id del torneo, el jugador 1, el jugador 2 y la ronda actual.
 *
 * @author Christian Escalas
 */
@Data
@Entity
@Table(name = "matches") // Ensure the table name is correctly set
public class Match {

    /**
     * Identificador único de la partida. No debe ser nulo
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    /**
     * Identificador único del torneo. No debe ser nulo
     */
    @Column(name = "tournament_id", nullable = false)
    private Long tournamentId;

    /**
     * Jugador 1 que participa en la partida. No debe ser nulo
     */
    @ManyToOne
    @JoinColumn(name = "player1_id", nullable = false)
    private Users player1;

    /**
     * Jugador 2 que participa en la partida. No debe ser nulo
     */
    @ManyToOne
    @JoinColumn(name = "player2_id", nullable = false)
    private Users player2;

    /**
     * Resultado de la partida. No debe ser nulo ni vacío.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "result")
    private Result result;

    /**
     * Ronda actual de la partida
     */
    @Column(name = "round", updatable = true, nullable = false)
    private Integer round;

    public Match() {
    }

    /**
     * Construye un nuevo torneo con nombre, número máximo de jugadores y estado.
     *
     * @param tournamentId id del torneo al que pertenece la partida.
     * @param player1      Jugador1 que participa en la partida. No puede ser null ni vacío.
     * @param player2      Jugador2 que participa en la partida. No puede ser null ni vacío.
     * @param result       resultado actual de la partida. No puede ser null ni vacío.
     */
    public Match(Long tournamentId, Users player1, Users player2, Result result) {
        this.tournamentId = tournamentId;
        this.player1 = player1;
        this.player2 = player2;
        this.result = result;
        this.round = 1;
    }
}