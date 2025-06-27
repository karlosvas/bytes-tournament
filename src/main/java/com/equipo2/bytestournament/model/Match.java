package com.equipo2.bytestournament.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.equipo2.bytestournament.enums.Result;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Match {
    public static final Long UMBRAL = 100L;

    /**
     * id: Identificador único de la partida que se genera automáticamente.
     * tournamentId: Identificador único del torneo al que pertenece la partida. No debe ser nulo.
     * player1: Jugador 1 que participa en la partida. No debe ser nulo.
     * player2: Jugador 2 que participa en la partida. No debe ser nulo.
     * result: Resultado de la partida. No debe ser nulo ni vacío.
     * round: Ronda actual de la partida. No debe ser nulo.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tournament_id", updatable = false, nullable = false)
    private Tournament tournament;

    @ManyToOne
    @JoinColumn(name = "player1", nullable = false)
    private User player1;

    @ManyToOne
    @JoinColumn(name = "player2", nullable = false)
    private User player2;

    @Enumerated(EnumType.STRING)
    @Column(name = "result", updatable = true, nullable = false)
    private Result result;

    @Column(name = "round", updatable = true, nullable = false)
    private Integer round;
}