package com.equipo2.bytestournament.enums;
/**
 * Enum que muestra los valores que puede tomar el atributo role
 *
 * Puede tomar los valores de ADMIN y PLAYER
 *
 * @author Christian Escalas
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