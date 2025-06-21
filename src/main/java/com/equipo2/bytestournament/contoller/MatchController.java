package com.equipo2.bytestournament.contoller;

import com.equipo2.bytestournament.DTO.MatchDTO;
import com.equipo2.bytestournament.annotations.SwaggerApiResponses;
import com.equipo2.bytestournament.service.MatchService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/matches")
public class MatchController {
    
    private final MatchService matchService;
    private final Logger logger = LoggerFactory.getLogger(MatchController.class);

    public MatchController(MatchService matchService){
        this.matchService = matchService;
    }

    @SwaggerApiResponses
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/generate/{tournamentId}")
    public List<MatchDTO> generateMatches(@PathVariable Long tournamentId) {
        // Se genera un un nuevo match para un torneo especifico
        logger.info("POST /api/matches/generate/{} - Generating matches for tournament ID: {}", tournamentId, tournamentId);
        return matchService.generateMatches(tournamentId);
    }
    
    @SwaggerApiResponses
    @GetMapping("/{id}")
    public MatchDTO checkMatch(@PathVariable Long id) {
        // Se comprueba un match por su ID
        logger.info("GET /api/matches/{} - Checking match with ID: {}", id, id);
        return matchService.checkMatch(id);
    }

    @SwaggerApiResponses
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/{id}/result")
    public MatchDTO updateMatchResult(@PathVariable Long id, @RequestBody MatchDTO macthDTO) {
        logger.info("PUT /api/matches/{}/result - Updating result for match ID: {}", id, id);
        return matchService.updateMatchResult(id, macthDTO);
    }

}
