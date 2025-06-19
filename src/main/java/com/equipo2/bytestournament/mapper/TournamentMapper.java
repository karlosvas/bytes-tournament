package com.equipo2.bytestournament.mapper;

import com.equipo2.bytestournament.DTO.TournamentDTO;
import com.equipo2.bytestournament.model.Tournament;
import org.mapstruct.Mapper;
import java.util.List;

/**
 * Mapper que convierte la entidad Tournament a DTO y la DTO a entidad
 *
 * @author Christian Escalas
 */
@Mapper(componentModel = "spring")
public interface TournamentMapper {
    TournamentDTO tournamentToTournamentDTO(Tournament tournament);

    Tournament tournamentDtoToTournament(TournamentDTO tournamentDTO);

    List<TournamentDTO> tournamentToTournamentDtos(List<Tournament> tournaments);

    List<Tournament> tournamentDtosToTournaments(List<TournamentDTO> tournamentDTOS);
}