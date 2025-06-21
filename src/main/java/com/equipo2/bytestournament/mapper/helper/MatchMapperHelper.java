package com.equipo2.bytestournament.mapper.helper;

import com.equipo2.bytestournament.model.Tournament;
import com.equipo2.bytestournament.model.User;
import com.equipo2.bytestournament.repository.UserRepository;
import com.equipo2.bytestournament.repository.TournamentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MatchMapperHelper {

    private final UserRepository userRepository;
    private final TournamentRepository tournamentRepository;
    private final Logger logger = LoggerFactory.getLogger(MatchMapperHelper.class.getName());

    public MatchMapperHelper(UserRepository userRepository, TournamentRepository tournamentRepository) {
        this.userRepository = userRepository;
        this.tournamentRepository = tournamentRepository;
    }

    public Tournament fromTournamentId(Long id) {
        try {
            return id == null ? null : tournamentRepository.findById(id).orElse(null);
        } catch (Exception e) {
            logger.error("Error al buscar el torneo con ID: " + id, e);
            return null; // Retorna null en caso de error
        }
    }

    public Long toTournamentId(Tournament tournament) {
        try {
            return tournament == null ? null : tournament.getId();
        } catch (Exception e) {
            logger.error("Error al obtener el ID del torneo: " + tournament, e);
            return null;
        }
    }

    public User fromId(Long id) {
        try {
            return id == null ? null : userRepository.findById(id).orElse(null);
        } catch (Exception e) {
            logger.error("Error al buscar el usuario con ID: " + id, e);
            return null; // Retorna null en caso de error
        }
    }

    public Long toId(User user) {
        try {
            return user == null ? null : user.getId();
        } catch (Exception e) {
            logger.error("Error al obtener el ID del usuario: " + user, e);
            return null; // Retorna null en caso de error
        }
    }
}