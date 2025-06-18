package com.equipo2.bytestournament.model;

import jakarta.persistence.*;

import com.equipo2.bytestournament.enums.Result;
import com.equipo2.bytestournament.model.User;

/**
 * Entidad JPA que representa una partida de un torneo de la aplicación de torneos.
 * <p>
 * Contiene el id del torneo, el jugador 1, el jugador 2 y la ronda actual.
 *
 * @author Christian Escalas
 */

@Entity
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
    @Column(name = "tournament_id", updatable = false, nullable = false)
    private Long tournamentId;

    /**
     * Jugador 1 que participa en la partida. No debe ser nulo
     */
    @Column(name = "player1", updatable = true, nullable = false)
    private User player1;

    /**
     * Jugador 2 que participa en la partida. No debe ser nulo
     */
    @Column(name = "player2", updatable = true, nullable = false)
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

        if (tournamentId == null) {
            throw new IllegalArgumentException("El torneo no puede ser nulo.");
        }
        if (player1 == null || player2 == null) {
            throw new IllegalArgumentException("Ninguno de los jugadores pueden ser nulos.");
        }
        if (player1.getId() == null || player2.getId() == null) {
            throw new IllegalArgumentException("Los jugadores deben tener id asignado");
        }
        if (player1.getId().equals(player2.getId())) {
            throw new IllegalArgumentException("Un jugador no puede competir contra sí mismo, escoge otro jugador.");
        }
        if (result == null) {
            throw new IllegalArgumentException("El resultado no puede ser null");
        }

        this.tournamentId = tournamentId;
        this.player1 = player1;
        this.player2 = player2;
        this.result = result;
        this.round = 1;
    }

    public Long getId() {
        return id;
    }

    public Long getTournamentId() {
        return tournamentId;
    }

    public User getPlayer1() {
        return player1;
    }

    public void setPlayer1(User player1) {
        if (player1 == null) {
            throw new IllegalArgumentException("El jugador no puede ser nulo.");
        }
        if (player1.getId() == null) {
            throw new IllegalArgumentException("El jugador debe tener id asignado.");
        }
        if (player1.getId().equals(player2.getId())) {
            throw new IllegalArgumentException("Un jugador no puede competir contra sí mismo, escoge otro jugador.");
        }

        this.player1 = player1;
    }

    public User getPlayer2() {
        return player2;
    }

    public void setPlayer2(User player2) {
        if (player2 == null) {
            throw new IllegalArgumentException("El jugador no puede ser nulo.");
        }
        if (player2.getId() == null) {
            throw new IllegalArgumentException("El jugador debe tener id asignado.");
        }
        if (player1.getId().equals(player2.getId())) {
            throw new IllegalArgumentException("Un jugador no puede competir contra sí mismo, escoge otro jugador.");
        }

        this.player2 = player2;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        if (result == null) {
            throw new IllegalArgumentException("El resultado no puede ser null");
        }

        this.result = result;
    }

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        if (round == null || round < this.round) {
            throw new IllegalArgumentException("La ronda debe ser igual o superior a la que ya está la partida.");
        }
        this.round = round;
    }
}