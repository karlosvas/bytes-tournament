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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/tournaments")
public class TournamentController {

    private final TournamentService tournamentService;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public TournamentController(TournamentService tournamentService, UserService userService) {
        this.tournamentService = tournamentService;
        this.userService = userService;
    }
    
    @SwaggerApiResponses
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping
    public TournamentDTO create(@RequestBody TournamentDTO tournamentDTO) {
        // Creación de un torneo
        logger.info("POST /api/tournaments - Torneo creado: {}", tournamentDTO);
        return tournamentService.createTournament(tournamentDTO);
    }
    
    @SwaggerApiResponses
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/players")
    public TournamentDTO joinTournament(@RequestParam Long tournamentId, @RequestParam String userName) {
        // El usuario actual que está autenticado se une al torneo
        logger.info("POST /api/tournaments/players/{} - User {} entro al torneo {}", tournamentId, userName, userName, tournamentId);
        TournamentDTO tournament = tournamentService.addPlayerToTournament(tournamentId, userName);
        return tournament;
    }

    @SwaggerApiResponses
    @PostMapping("/players/me")
    public ResponseEntity<TournamentDTO> joinTournamentWithActualUser(@RequestParam Long tournamentId, Authentication authentication) {
        // El usuario actual que está autenticado se une al torneo
        logger.info("POST /api/tournaments/players/me{} - User {} entro al torneo {}", tournamentId, authentication.getName(), tournamentId);
        String name = userService.getUserNameFronAutentication(authentication);
        TournamentDTO tournament = tournamentService.addPlayerToTournament(tournamentId, name);
        return ResponseEntity.ok(tournament);
    }

    @SwaggerApiResponses
    @GetMapping("/ranking/{tournamentId}")
    public ResponseEntity<?> getClassification(@PathVariable Long tournamentId, @RequestParam(value = "pretty", required = false) boolean pretty) {
        logger.info("GET /api/tournaments/ranking/{}?type={}", tournamentId, pretty);

        // Si tiene el parametro pretty truie devuelve el ranking en formato bonito (String), por defecto es false ya que no es requerido
        // En el caso de que no esté devuelve el DTO normal (JSON)
        var ranking = tournamentService.getClassification(tournamentId);
        return ResponseEntity.ok(pretty ? ranking.toString() : ranking);
    }

    @SwaggerApiResponses
    @GetMapping("/ranking/details/{tournamentId}")
    public ResponseEntity<?> getRankingDetails(@PathVariable Long tournamentId, @RequestParam(value = "pretty", required = false) boolean pretty) {
        logger.info("GET /api/tournaments/ranking/details/{}?type={}", tournamentId, pretty);


        // Si tiene el parametro pretty truie devuelve el ranking en formato bonito (String), por defecto es false ya que no es requerido
        // En el caso de que no esté devuelve el DTO normal (JSON)
        var ranking = tournamentService.getRankingDetails(tournamentId);
        return ResponseEntity.ok((pretty) ?  ranking.toString() : ranking);
    }
}
