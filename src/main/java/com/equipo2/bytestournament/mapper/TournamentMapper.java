package com.equipo2.bytestournament.model.mapper;

import com.equipo2.bytestournament.DTO.TournamentDTO;
import com.equipo2.bytestournament.model.Tournament;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.ArrayList;

/**
 * Mapper que convierte la entidad Tournament a DTO y la DTO a entidad
 *
 * @author Christian Escalas
 */
@Component
public class TournamentMapper {

    private final TournamentRepository tournamentRepository;

    /**
     * Constructor para inyectar las dependencias.
     *
     * @param userRepository repositorio de usuarios.
     */
    public TournamentMapper(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    /**
     * Convierte un TournamentDTO en una instancia de Tournament.
     * <p>
     * Si TournamentDTO es null, devuelve null.
     *
     * @param tournamentDTO con los datos de la partida.
     * @return una entidad Tournament con los campos asignados desde el DTO.
     */
    public Tournament tournamentDtoToTournament(TournamentDTO tournamentDTO) {

        if (tournamentDTO == null) {
            return null;
        }

        Tournament tournament = new Tournament();

        tournament.setName(tournamentDTO.getName());
        tournament.setMaxPlayers(tournamentDTO.getMaxPlayers());
        tournament.setStatus(tournamentDTO.getStatus());

        return tournament;
    }

    /**
     * Convierte una entidad Tournament en TournamentDTO.
     * <p>
     * Si tournament es null, devuelve null.
     *
     * @param tournament entidad que hay que convertir a DTO.
     * @return DTO con los mismos valores que la entidad.
     */
    public TournamentDTO tournamentToTournamentDTO(Tournament tournament) {

        if (tournament == null) {
            return null;
        }

        TournamentDTO tournamentDTO = new TournamentDTO();

        tournamentDTO.setId(tournament.getId());
        tournamentDTO.setName(tournament.getName());
        tournamentDTO.setMaxPlayers(tournament.getMaxPlayers());
        tournamentDTO.setStatus(tournament.getStatus());

        return tournamentDTO;
    }

    /**
     * Convierte una lista de entidades Tournament a una lista de TournamentDTO.
     * <p>
     * Si tournaments es null, devuelve una lista vacía.
     * Cada elemento tournament de la lista se convierte a TournamentDTO.
     *
     * @param tournaments lista de entidades Tournament. Puede ser null.
     * @return lista de DTOs, pero si la lista tournaments está vacía devuelve una lista vacía.
     */
    public List<TournamentDTO> tournamentTotournamentDtos(List<Tournament> tournaments) {

        if (tournaments == null) {
            return List.of();
        }

        List<TournamentDTO> tournamentsDtos = new ArrayList<>();
        for (Tournament tournament : tournaments) {
            tournamentsDtos.add(tournamentToTournamentDTO(tournament));
        }
        return tournamentsDtos;
    }

    /**
     * Actualiza los campos de la entidad Tournament.
     * <p>
     * Si tournamentDTO es null o tournament es null, no hace nada.
     *
     * @param tournament    entidad que se va a actualizar. Si es null, no hace nada.
     * @param tournamentDTO con los nuevos valores. Si es null, no hace nada.
     */
    public void updateTournament(Tournament tournament, TournamentDTO tournamentDTO) {

        if (tournament == null || tournamentDTO == null) {
            return;
        } else {
            tournament.setName(tournamentDTO.getName());
            tournament.setMaxPlayers(tournamentDTO.getMaxPlayers());
            tournament.setStatus(tournamentDTO.getStatus());
        }
    }
}