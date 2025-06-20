package com.equipo2.bytestournament.model;

import jakarta.persistence.*;
import com.equipo2.bytestournament.enums.Status;
import lombok.Data;


/**
 * Entidad JPA que representa un torneo de la aplicación de torneos.
 * <p>
 * Contiene el nombre del torneo, los jugadores máximos que participan y el estado.
 *
 * @author Christian Escalas
 */
@Data
@Entity
@Table(name = "tournaments")
public class Tournament {

    /**
     * Identificador único del torneo que se genera automaticamente
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    /**
     * Nombre del torneo. Debe ser único y no nulo.
     */
    @Column(name = "name", updatable = true, nullable = false, unique = true)
    private String name;

    /**
     * Cantidad máxima de jugadores que pueden participar en el torneo. No puede ser nulo.
     */
    @Column(name = "max_players", updatable = true, nullable = false)
    private Integer maxPlayers;

    /**
     * Estado asignado al usuario. No debe ser nulo ni vacío.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", updatable = true, nullable = false)
    private Status status;

    public Tournament() {}

    /**
     * Construye un nuevo torneo con nombre, número máximo de jugadores y estado.
     *
     * @param name       nombre de usuario. No puede ser null ni vacío.
     * @param maxPlayers cantidad máxima de jugadores participantes .
     * @param status     estado actual del torneo. No puede ser null ni vacío.
     */
    public Tournament(String name, Integer maxPlayers, Status status) {
        this.name = name;
        this.maxPlayers = maxPlayers;
        this.status = status;
    }
}