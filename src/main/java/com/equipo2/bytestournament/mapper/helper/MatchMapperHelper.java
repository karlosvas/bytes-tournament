package com.equipo2.bytestournament.mapper.helper;

import com.equipo2.bytestournament.enums.ApiResponse;
import com.equipo2.bytestournament.exceptions.RequestException;
import com.equipo2.bytestournament.model.Tournament;
import com.equipo2.bytestournament.model.User;
import com.equipo2.bytestournament.repository.UserRepository;
import com.equipo2.bytestournament.repository.TournamentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Helper para asistir al MatchMapper con conversiones complejas
 * entre entidades y DTOs.
 * Este helper proporciona métodos para convertir entre IDs y entidades
 * de tipo Tournament y User, manejando excepciones y errores de manera adecuada.
 * 
 * @Component Anotación de Spring que indica que esta clase es un componente
 * que puede ser inyectado en otros componentes de la aplicación.
 */
@Component
public class MatchMapperHelper {

    /**
     * userRepository Repositorio para acceder a los usuarios.
     * tournamentRepository Repositorio para acceder a los torneos.
     * Logger  para registrar mensajes de error y depuración.
     */
    private final UserRepository userRepository;
    private final TournamentRepository tournamentRepository;
    private final Logger logger = LoggerFactory.getLogger(MatchMapperHelper.class.getName());

    public MatchMapperHelper(UserRepository userRepository, TournamentRepository tournamentRepository) {
        this.userRepository = userRepository;
        this.tournamentRepository = tournamentRepository;
    }

    /**
     * Convierte un Long ID de torneo a una entidad Tournament.
     * 
     * @param tournamentId ID del torneo a convertir.
     * @return Entidad Tournament correspondiente al ID, o null si el ID es null
     */
    public Tournament fromTournamentId(Long tournamentId) {
        try {
            return tournamentId == null ? null : tournamentRepository.findById(tournamentId).orElse(null);
        } catch (Exception e) {
            logger.error("Error al buscar el torneo con ID: " + tournamentId, e);
            throw new RequestException(ApiResponse.NOT_FOUND, "Torneo no encontrado",  "No se pudo establecer la relación con el torneo con ID: " + tournamentId);
        }
    }

    /**
     * Convierte una entidad Tournament a su Long ID.
     * 
     * @param tournament Entidad Tournament a convertir.
     * @return ID del torneo, o null si la entidad es null
     */
    public Long toTournamentId(Tournament tournament) {
        try {
            return tournament == null ? null : tournament.getId();
        } catch (Exception e) {
            logger.error("Error al obtener el ID del torneo: " + tournament, e);
            throw new RequestException(ApiResponse.NOT_FOUND, "Torneo no encontrado",  "No se pudo establecer la relación con el torneo");
        }
    }

    /**
     * Convierte un Long ID de usuario a una entidad User.
     * 
     * @param matchId ID del usuario a convertir.
     * @return Entidad User correspondiente al ID, o null si el ID es null
     */
    public User fromId(Long id) {
        try {
            return id == null ? null : userRepository.findById(id).orElse(null);
        } catch (Exception e) {
            logger.error("Error al buscar el usuario con ID: " + id, e);
            throw new RequestException(ApiResponse.NOT_FOUND, "Usuario no encontrado", "No se pudo establecer la relación con el usuario");
        }
    }

    /**
     * Convierte una entidad User a su Long ID.
     * 
     * @param user Entidad User a convertir.
     * @return ID del usuario, o null si la entidad es null
     */
    public Long toId(User user) {
        try {
            return user == null ? null : user.getId();
        } catch (Exception e) {
            logger.error("Error al obtener el ID del usuario: " + user, e);
            throw new RequestException(ApiResponse.NOT_FOUND, "Usuario no encontrado", "No se pudo establecer la relación con el usuario");
        }
    }
}