package com.equipo2.bytestournament.mapper;

import com.equipo2.bytestournament.DTO.MatchDTO;
import com.equipo2.bytestournament.mapper.helper.MatchMapperHelper;
import com.equipo2.bytestournament.model.Match;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

/**
 * Mapper que convierte la entidad Match a DTO y la DTO a entidad
 * y viceversa.
 * Utiliza MapStruct para generar el código de mapeo automáticamente.
 * 
 * Utiliza un helper {@link MatchMapperHelper} para mapear
 * los campos que no se pueden mapear directamente debido a la complejidad como las relaciones
 * entre entidades.
 * 
 * {@link Mapper} Anotación de MapStruct que indica que esta interfaz es un mapper.
 * {@link Mapping} Anotación de MapStruct que indica cómo se deben mapear
 */

@Mapper(componentModel = "spring", uses = {MatchMapperHelper.class})
public interface MatchMapper {
    @Mapping(source = "tournament", target = "tournament")
    @Mapping(source = "player1", target = "player1")
    @Mapping(source = "player2", target = "player2")
    Match matchDtoToMatch(MatchDTO matchDTO);

    @Mapping(source = "tournament", target = "tournament")
    @Mapping(source = "player1", target = "player1")
    @Mapping(source = "player2", target = "player2")
    List<Match> matchDTOListToMatchList(List<MatchDTO> matchDTOs);

    @Mapping(source = "tournament", target = "tournament")
    @Mapping(source = "player1", target = "player1")
    @Mapping(source = "player2", target = "player2")
    List<MatchDTO> matchListToMatchDTOList(List<Match> matches);

    @Mapping(source = "tournament", target = "tournament")
    @Mapping(source = "player1", target = "player1")
    @Mapping(source = "player2", target = "player2")
    MatchDTO matchToMatchDTO(Match match);
}