import jakarta.persistence.*;

/**
 * Entidad JPA que representa una partida de un torneo de la aplicación de torneos.
 *
 * Contiene el id del torneo, el jugador 1, el jugador 2 y la ronda actual.
 *
 * @author Christian Escalas
 */

@Entity
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    /**
     * Identificador único de la partida. No debe ser nulo
     */
    @Column(name = "id", updatable = false, nullable = false)
    private long id;

    /**
     * Identificador único del torneo. No debe ser nulo
     */
    @Column(name = "tournament_id", updatable = false, nullable = false)
    private long tournamentId;

    /**
     * Jugador 1 que participa en la partida. No debe ser nulo
     */
    @Column(name = "player1", updatable = false, nullable = false)
    private User player1;

    /**
     * Jugador 2 que participa en la partida. No debe ser nulo
     */
    @Column(name = "player2", updatable = false, nullable = false)
    private User player2;

    /**
     * Resultado de la partida. No debe ser nulo ni vacío.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "result", nullable = false)
    private Result result;

    /**
     * Ronda actual de la partida
     */
    @Column(name = "round", updatable = true, nullable = false)
    private int round;

    public Match() {
    }

    /**
     * Construye una nueva partida con el id del torneo, el jugador1, el jugador 2, el resultado y la ronda.
     *
     * @param tournamentId id del torneo al que pertenece la partida
     * @param player1 jugador nº 1 de la partida.
     * @param player2 jugador nº 2 de la partida.
     * @param result resultado actual de la partida.
     * @param round ronda actual de la partida
     */
    public Match(long tournamentId, User player1, User player2, Result result, int round) {

        this.tournamentId = tournamentId;
        this.player1 = player1;

        if (player1.getId() == player2.getId()) {
            throw new IllegalArgumentException("Un jugador no puede competir contra sí mismo, escoge otro jugador.");
        } else {
            this.player2 = player2;
        }

        this.result = result;

        if (round < 1) {
            throw new IllegalArgumentException("La ronda debe ser al menos la 1.")
        } else {
            this.round = round;
        }
    }

    public long getId() {
        return id;
    }

    public long getTournamentId() {
        return tournamentId;
    }

    public User getPlayer1() {
        return player1;
    }

    public void setPlayer1(User player1) {
        this.player1 = player1;
    }

    public User getPlayer2() {
        return player2;
    }

    public void setPlayer2(User player2) {

        if (player1.getId() == player2.getId()) {
            throw new IllegalArgumentException("Un jugador no puede competir contra sí mismo, escoge otro jugador.")
        } else {
            this.player2 = player2;
        }
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {

        if (round < 1) {
            throw new IllegalArgumentException("La ronda debe ser al menos la 1.")
        } else {
            this.round = round;
        }
    }
}