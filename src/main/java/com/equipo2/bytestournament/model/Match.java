package com.equipo2.bytestournament.model;

import jakarta.persistence.*;
import com.equipo2.bytestournament.enums.Result;
import com.equipo2.bytestournament.model.User;
import lombok.Data;

/**
 * Entidad JPA que representa una partida de un torneo de la aplicación de torneos.
 * <p>
 * Contiene el id del torneo, el jugador 1, el jugador 2 y la ronda actual.
 *
 * @author Christian Escalas
 */
@Data
@Entity
@Table(name = "matches")
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
    // TODO: Relación entre Match y Tournament
    @Column(name = "tournament_id", updatable = false, nullable = false)
    private Long tournamentId;

    /**
     * Jugador 1 que participa en la partida. No debe ser nulo
     */
    @ManyToOne
    @JoinColumn(name = "player1_id", nullable = false)
    private User player1;

    /**
     * Jugador 2 que participa en la partida. No debe ser nulo
     */
    @ManyToOne
    @JoinColumn(name = "player2_id", nullable = false)
    private User player2;

    /**
     * Resultado de la partida. No debe ser nulo ni vacío.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "result", updatable = true, nullable = false)
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
     * @param round        ronda actual de la partida.
     */
    public Match(Long tournamentId, User player1, User player2, Result result) {
        this.tournamentId = tournamentId;
        this.player1 = player1;
        this.player2 = player2;
        this.result = result;
        this.round = 1;
    }
}