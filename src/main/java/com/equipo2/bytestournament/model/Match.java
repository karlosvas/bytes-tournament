package com.equipo2.bytestournament.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.equipo2.bytestournament.enums.Result;

/**
 * Entidad JPA que representa una partida de un torneo.
 * Contiene el id del torneo, el jugador 1, el jugador 2 y la ronda actual.
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "matches")
public class Match {
    public static final Integer UMBRAL = 100;

    /**
     * id: Entidad JPA que representa una partida de un torneo de la aplicación de torneos.
     * tournament: Contiene el id del torneo, el jugador 1, el jugador 2 y la ronda actual.
     * player1: Jugador 1 que participa en la partida. No debe ser nulo
     * player2: Jugador 2 que participa en la partida. No debe ser nulo
     * result: Resultado de la partida. No debe ser nulo ni vacío.
     * round: Ronda actual de la partida
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tournament_id", referencedColumnName = "id")
    private Tournament tournament;

    @ManyToOne
    @JoinColumn(name = "player1_id", nullable = false)
    private User player1;

    @ManyToOne
    @JoinColumn(name = "player2_id", nullable = false)
    private User player2;

    @Enumerated(EnumType.STRING)
    @Column(name = "result")
    private Result result;

    @Column(name = "round", updatable = true, nullable = false)
    private Integer round;
}