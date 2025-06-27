package com.equipo2.bytestournament.enums;
/**
 * Enum Role que muestra los valores que puede tomar el atributo role
 *
 * Los valores que puede tomar son:
 * ADMIN: Indica que el usuario tiene privilegios de administrador.
 * PLAYER: Indica que el usuario es un jugador normal.
 */

public enum Role {
    ADMIN("ADMIN"),
    PLAYER("PLAYER");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}