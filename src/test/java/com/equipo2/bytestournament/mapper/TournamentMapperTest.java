package com.equipo2.bytestournament.mapper;

import com.equipo2.bytestournament.DTO.TournamentDTO;
import com.equipo2.bytestournament.enums.Status;
import com.equipo2.bytestournament.mapper.helper.TournamentMapperHelper;
import com.equipo2.bytestournament.model.Match;
import com.equipo2.bytestournament.model.Tournament;
import com.equipo2.bytestournament.model.User;
import com.equipo2.bytestournament.repository.MatchRepository;
import com.equipo2.bytestournament.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TournamentMapperTest {

    private static TournamentMapper tournamentMapper;
    private static UserRepository userRepository;
    private static MatchRepository matchRepository;
    
    @BeforeAll
    static void setUp() throws Exception {
        tournamentMapper = Mappers.getMapper(TournamentMapper.class);
        Field helperField = tournamentMapper.getClass().getDeclaredField("tournamentMapperHelper");
        TournamentMapperHelper helper = new TournamentMapperHelper(userRepository, matchRepository);
        helperField.setAccessible(true);
        helperField.set(tournamentMapper, helper);
    }

    @Test
    void testTournamentToTournamentDTO() {
        User user1 = User.builder().id(1L).build();
        User user2 = User.builder().id(2L).build();
        Match match1 = Match.builder().id(10L).build();
        Match match2 = Match.builder().id(20L).build();

        Tournament tournament = Tournament.builder()
                .id(100L)
                .name("Torneo Test")
                .maxPlayers(8)
                .status(Status.PENDING)
                .rounds(2)
                .maxRounds(5)
                .matches(new ArrayList<>(List.of(match1, match2)))
                .players(new ArrayList<>(List.of(user1, user2)))
                .build();

        TournamentDTO dto = tournamentMapper.tournamentToTournamentDTO(tournament);

        assertNotNull(dto);
        assertEquals(tournament.getId(), dto.getId());
        assertEquals(tournament.getName(), dto.getName());
        assertEquals(tournament.getMaxPlayers(), dto.getMaxPlayers());
        assertEquals(tournament.getStatus(), dto.getStatus());
        assertEquals(tournament.getRounds(), dto.getRounds());
        assertEquals(tournament.getMaxRounds(), dto.getMaxRounds());
        assertEquals(2, dto.getMatches().size());
        assertEquals(2, dto.getPlayers().size());
        assertTrue(dto.getPlayers().contains(1L));
        assertTrue(dto.getPlayers().contains(2L));
    }

    @Test
    void testTournamentDTOToTournament() {
        Match match1 = Match.builder().id(10L).build();
        Match match2 = Match.builder().id(20L).build();

        TournamentDTO dto = TournamentDTO.builder()
                .id(200L)
                .name("Torneo DTO")
                .maxPlayers(16)
                .status(Status.FINISHED)
                .rounds(3)
                .maxRounds(7)
                .matches(new ArrayList<>(List.of(match1, match2)))
                .players(new ArrayList<>(List.of(11L, 22L)))
                .build();

        Tournament tournament = tournamentMapper.tournamentDtoToTournament(dto);

        assertNotNull(tournament);
        assertEquals(dto.getId(), tournament.getId());
        assertEquals(dto.getName(), tournament.getName());
        assertEquals(dto.getMaxPlayers(), tournament.getMaxPlayers());
        assertEquals(dto.getStatus(), tournament.getStatus());
        assertEquals(dto.getRounds(), tournament.getRounds());
        assertEquals(dto.getMaxRounds(), tournament.getMaxRounds());
        assertEquals(2, tournament.getMatches().size());
        // Los usuarios pueden requerir un helper para mapear los IDs a entidades reales
    }
}