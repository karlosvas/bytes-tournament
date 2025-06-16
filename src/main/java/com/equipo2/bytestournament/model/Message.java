import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad JPA que representa un mensaje en el chat de una partida de la aplicación de torneos.
 *
 * Contiene el id del mensaje, el id del jugador que lo envía, el contenido, la fecha y hora de envío del mensaje, el id de la partidaa y el id del torneo.
 *
 * @author Christian Escalas
 */
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    /**
     * Identificador único del mensaje. No debe ser nulo
     */
    @Column(name = "id", updatable = false, nullable = false)
    private long id;

    /**
     * Identificador único del jugador que envía el mensaje. No debe ser nulo
     */
    @Column(name = "sender_id", updatable = false, nullable = false)
    private long senderId;

    /**
     * Contenido del mensaje enviado. No puede ser nulo.
     */
    @Column(name = "content", updatable = true, nullable = false)
    private String content;

    /**
     * Fecha y hora de envío del mensaje. No puede ser nulo.
     */
    @Column(name = "timestamp", updatable = false, nullable = false)
    private LocalDateTime timestamp;

    /**
     * Identificador único del torneo. No debe ser nulo
     */
    @Column(name = "match_id", updatable = false, nullable = false)
    private long matchId;

    /**
     * Identificador único del torneo que se genera automaticamente
     */
    @Column(name = "tournament_id", updatable = false, nullable = false)
    private long tournamentId;

    public Message() {
    }

    /**
     * Construye un nuevo mensaje con el id del jugador que lo manda, el contenido, la fecha y hora del envío,
     * el id de la partida y el id del torneo al que pertenece la partida.
     *
     * @param senderId     id del jugador que envía el mensaje que participa en la partida. No puede ser null ni vacío.
     * @param content      contenido del mensaje.
     * @param timestamp    fecha y hora en la que se envía el mensaje.
     * @param matchId      id de la partida
     * @param tournamentId id del torneo al que pertenece la partida
     */
    public Message(long senderId, String content, LocalDateTime timestamp, long matchId, long tournamentId) {

        this.senderId = senderId;
        this.content = content;
        this.timestamp = timestamp;
        this.matchId = matchId;
        this.tournamentId = tournamentId;
    }

    public long getId() {
        return id;
    }

    public long getSenderId() {
        return senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public long getMatchId() {
        return matchId;
    }

    public long getTournamentId() {
        return tournamentId;
    }
}