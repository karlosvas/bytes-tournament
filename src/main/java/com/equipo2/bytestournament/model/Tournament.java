package com.equipo2.bytestournament.model;
import jakarta.persistence.*;
import java.util.List;

import com.equipo2.bytestournament.enums.Status;

import java.util.ArrayList;

/**
 * Entidad JPA que representa un torneo de la aplicación de torneos.
 *
 * Contiene el nombre del torneo, los jugadores máximos que participan y el estado.
 *
 * @author Christian Escalas
 */

@Entity
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    /**
     * Identificador único del torneo que se genera automaticamente
     */
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    /**
     * Nombre del torneo. Debe ser único y no nulo.
     */
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    private Integer maxPlayers;

    /**
     * Estado asignado al usuario. No debe ser nulo ni vacío.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    public Tournament() {
    }

    /**
     * Construye un nuevo torneo con nombre, número máximo de jugadores y estado.
     *
     * @param name nombre de usuario. No puede ser null ni vacío.
     * @param maxPlayers cantidad máxima de jugadores participantes .
     * @param status estado actual del torneo. No puede ser null ni vacío.
     */
    public Tournament(String name, Integer maxPlayers, Status status) {

        this.name = name;
        this.maxPlayers = maxPlayers;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {

        if (name.trim().equalsIgnoreCase(this.name)) {
            throw new IllegalArgumentException("No puedes repetir el mismo nombre.");
        } else {
            this.name = name;
        }
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {

        if (maxPlayers < 2) {
            throw new IllegalArgumentException("Deben participar al menos 2 jugadores.");
        } else {
            this.maxPlayers = maxPlayers;
        }
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}