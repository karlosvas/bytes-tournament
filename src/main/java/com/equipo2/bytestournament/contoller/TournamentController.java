package com.equipo2.bytestournament.contoller;

import org.springframework.web.bind.annotation.RestController;
import com.equipo2.bytestournament.DTO.TournamentDTO;
import com.equipo2.bytestournament.annotations.SwaggerApiResponses;
import com.equipo2.bytestournament.service.TournamentService;
import com.equipo2.bytestournament.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/tournaments")
public class TournamentController {
    private final TournamentService tournamentService;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }
    
    @SwaggerApiResponses
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping
    public TournamentDTO create(@RequestBody TournamentDTO tournamentDTO) {
        // Creación de un torneo
        logger.info("POST /api/tournaments - Creating tournament: {}", tournamentDTO);
        return tournamentService.createTournament(tournamentDTO);
    }
    
    @SwaggerApiResponses
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/players")
    public ResponseEntity<TournamentDTO> joinTournament(@RequestParam Long tournamentId, @RequestParam String userName) {
        // El usuario actual que está autenticado se une al torneo
        logger.info("POST /api/tournaments/{}/players/{} - User {} joining tournament {}", tournamentId, userName, userName, tournamentId);
        TournamentDTO tournament = tournamentService.addPlayerToTournament(tournamentId, userName);
        return ResponseEntity.ok(tournament);
    }
}
