package com.equipo2.bytestournament.model;

import jakarta.persistence.*;
import com.equipo2.bytestournament.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA que representa un torneo de la aplicación de torneos.
 * Contiene el nombre del torneo, los jugadores máximos que participan y el estado.
 *
 * @author Christian Escalas
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tournaments")
public class Tournament {
    /**
     * id: Identificador único del torneo que se genera automaticamente
     * name: Nombre del torneo. Debe ser único y no nulo.
     * maxPlayers: Cantidad máxima de jugadores que pueden participar en el torneo. No puede ser nulo.
     * status: Estado asignado al usuario. No debe ser nulo ni vacío.
     * rounds: Número de ronda actual del torneo.
     * maxRounds: Número máximo de rondas del torneo.
     * matchesList: Lista de partidos asociados al torneo.
     * players: Lista de jugadores que participan en el torneo.
     * un torneo puede tener múltiples jugadores y un jugador puede participar en múltiples torneos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name", updatable = true, nullable = false, unique = true)
    private String name;

    @Column(name = "max_players", updatable = true, nullable = false)
    private Integer maxPlayers;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", updatable = true, nullable = false)
    private Status status;

    @Column(name = "rounds", updatable = true, nullable = false)
    private Integer rounds;

    @Column(name = "max_rounds", updatable = true, nullable = false)
    private Integer maxRounds;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Match> matches = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "tournament_players",
            joinColumns = @JoinColumn(name = "tournament_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> players = new ArrayList<>();
}