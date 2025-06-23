package com.equipo2.bytestournament.enums;

/**
 * Enum Rank representa los diferentes rangos que un usuario puede alcanzar en la aplicación.
 * Cada rango indica el nivel de habilidad o logro del usuario en la plataforma.
 * 
 * Los rangos son:
 * - BRONZE: Representa el nivel inicial o de principiante.
 * - SILVER: Indica un nivel intermedio de habilidad.
 * - GOLD: Representa un nivel avanzado de habilidad.
 * - PLATINUM: Indica un nivel superior de habilidad.
 * - DIAMOND: Representa un nivel de habilidad muy alto.
 * - MASTER: Indica un nivel de habilidad excepcional.
 * - GRANDMASTER: Representa el nivel más alto de habilidad, reservado para los mejores jugadores
 */
public enum Rank {
    BRONZE(0, 400),
    SILVER(401, 800),
    GOLD(801, 1200),
    PLATINUM(1201, 1600),
    DIAMOND(1601, 2000),
    MASTER(2001, 2400),
    GRANDMASTER(2401, 3000);

    private final Integer minPoints;
    private final Integer maxPoints;

    Rank(Integer minPoints, Integer maxPoints) {
        this.minPoints = minPoints;
        this.maxPoints = maxPoints;
    }

    public Integer getMinPoints() {
        return minPoints;
    }

    public Integer getMaxPoints() {
        return maxPoints;
    }

    // Método para obtener el Rank según los puntos
    public static Rank fromPoints(Integer points) {
        for (Rank rank : Rank.values()) 
            if (points >= rank.minPoints && points <= rank.maxPoints) 
                return rank;
        return BRONZE;
    }
}
