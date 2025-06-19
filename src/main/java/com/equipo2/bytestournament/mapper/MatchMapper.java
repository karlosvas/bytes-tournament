package com.equipo2.bytestournament.mapper;

import com.equipo2.bytestournament.DTO.MatchDTO;
import com.equipo2.bytestournament.model.Match;
import org.mapstruct.Mapper;
import com.equipo2.bytestournament.repository.MatchRepository;
import org.springframework.stereotype.Component;
import java.util.List;
/**
 * Mapper que convierte la entidad Match a DTO y la DTO a entidad
 *
 * @author Christian Escalas
 */
@Mapper(componentModel = "spring")
public interface MatchMapper {
    MatchDTO matchToMatchDTO(Match match);

    Match matchDtoToMatch(MatchDTO matchDTO);

    List<MatchDTO> matchToMatchDtos(List<Match> matches);
    
    List<Match> matchDtosToMatches(List<MatchDTO> matchDTOs);
}