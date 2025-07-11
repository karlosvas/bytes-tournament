package com.equipo2.bytestournament.mapper;

import com.equipo2.bytestournament.DTO.TournamentDTO;
import com.equipo2.bytestournament.mapper.helper.TournamentMapperHelper;
import com.equipo2.bytestournament.model.Tournament;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper que convierte la entidad Tournament a DTO y la DTO a entidad
 * y viceversa.
 * Utiliza MapStruct para generar el código de mapeo automáticamente.
 * 
 * Utiliza un helper {@link TournamentMapperHelper} para mapear
 * los campos que no se pueden mapear directamente debido a la complejidad como las relaciones
 * entre entidades.
 * 
 * {@link Mapper} Anotación de MapStruct que indica que esta interfaz es un mapper.
 * {@link Mapping} Anotación de MapStruct que indica cómo se deben mapear
 */ 

@Mapper(componentModel = "spring", uses = {TournamentMapperHelper.class})
public interface TournamentMapper {
    // Tournament -> TournamentDTO
    @Mapping(target = "matches", source = "matches")
    @Mapping(target = "players", source = "players")
    TournamentDTO tournamentToTournamentDTO(Tournament tournament);

    // TournamentDTO -> Tournament
    @Mapping(target = "matches", source = "matches")
    @Mapping(target = "players", source = "players")
    Tournament tournamentDtoToTournament(TournamentDTO tournamentDTO);

    // List<TournamentDTO> -> List<Tournament>
    @Mapping(target = "matches", source = "matches")
    @Mapping(target = "players", source = "players")
    List<Tournament> tournamentDTOListToTournamentList(List<TournamentDTO> listTournamentsDTOs);

    // List<Tournament> -> List<TournamentDTO>
    @Mapping(target = "matches", source = "matches")
    @Mapping(target = "players", source = "players")
    List<TournamentDTO> tournamentListToTournamentDTOList(List<Tournament> listTournaments);
}