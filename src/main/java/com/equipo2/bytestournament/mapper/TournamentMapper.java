package com.equipo2.bytestournament.mapper;

import com.equipo2.bytestournament.DTO.TournamentDTO;
import com.equipo2.bytestournament.mapper.helper.TournamentMapperHelper;
import com.equipo2.bytestournament.model.Tournament;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper que convierte la entidad Tournament a DTO y la DTO a entidad
 *
 * @author Christian Escalas
 */
@Mapper(componentModel = "spring", uses = {TournamentMapperHelper.class})
public interface TournamentMapper {
    @Mapping(target = "matches", source = "matches")
    @Mapping(target = "players", source = "players")
    TournamentDTO tournamentToTournamentDTO(Tournament tournament);

    @Mapping(target = "matches", source = "matches")
    @Mapping(target = "players", source = "players")
    Tournament tournamentDtoToTournament(TournamentDTO tournamentDTO);
}