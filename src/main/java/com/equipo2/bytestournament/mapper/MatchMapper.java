package com.equipo2.bytestournament.model.mapper;

import com.equipo2.bytestournament.DTO.MatchDTO;
import com.equipo2.bytestournament.model.Match;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.ArrayList;

/**
 * Mapper que convierte la entidad Match a DTO y la DTO a entidad
 *
 * @author Christian Escalas
 */
@Component
public class MatchMapper {

    private final MatchRepository matchRepository;

    /**
     * Constructor para inyectar las dependencias.
     *
     * @param userRepository repositorio de usuarios.
     */
    public MatchMapper(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    /**
     * Convierte un MatchDTO en una instancia de Match.
     * <p>
     * Si matchDTO es null, devuelve null.
     *
     * @param matchDTO con los datos de la partida.
     * @return una entidad Match con los campos asignados desde el DTO.
     */
    public Match matchDtoToMatch(MatchDTO matchDTO) {

        if (matchDTO == null) {
            return null;
        }

        Match match = new Match();

        match.setPlayer1(matchDTO.getPlayer1());
        match.setPlayer2(matchDTO.getPlayer2());
        match.setResult(matchDTO.getResult());
        match.setRound(matchDTO.getRound());

        return match;
    }

    /**
     * Convierte una entidad Match en MatchDTO.
     * <p>
     * Si match es null, devuelve null.
     *
     * @param match entidad que hay que convertir a DTO.
     * @return DTO con los mismos valores que la entidad.
     */
    public MatchDTO matchToMatchDTO(Match match) {

        if (match == null) {
            return null;
        }

        MatchDTO matchDTO = new MatchDTO();

        matchDTO.setId(match.getId());
        matchDTO.setTournamentId(match.getTournamentId());
        matchDTO.setPlayer1(match.getPlayer1());
        matchDTO.setPlayer2(match.getPlayer2());
        matchDTO.setResult(match.getResult());
        matchDTO.setRound(match.getRound());

        return matchDTO;
    }

    /**
     * Convierte una lista de entidades Match a una lista de MatchDTO.
     * <p>
     * Si matches es null, devuelve una lista vacía.
     * Cada elemento match de la lista se convierte a MatchDTO.
     *
     * @param matches lista de entidades Match. Puede ser null.
     * @return lista de DTOs, pero si la lista matches está vacía devuelve una lista vacía.
     */
    public List<MatchDTO> matchToMatchDtos(List<Match> matches) {

        if (matches == null) {
            return List.of();
        }

        List<MatchDTO> matchDtos = new ArrayList<>();
        for (Match match : matches) {
            matchDtos.add(matchToMatchDTO(match));
        }
        return matchDtos;
    }

    /**
     * Actualiza los campos de la entidad Match.
     * <p>
     * Si matchDTO es null o match es null, no hace nada.
     *
     * @param match    entidad que se va a actualizar. Si es null, no hace nada.
     * @param matchDTO con los nuevos valores. Si es null, no hace nada.
     */
    public void updateMatch(Match match, MatchDTO matchDTO) {

        if (match == null || matchDTO == null) {
            return;
        } else {
            match.setPlayer1(matchDTO.getPlayer1());
            match.setPlayer2(matchDTO.getPlayer2());
            match.setResult(matchDTO.getResult());
            match.setRound(matchDTO.getRound());
        }
    }
}