package com.equipo2.bytestournament.contoller;

import com.equipo2.bytestournament.DTO.MatchDTO;
import com.equipo2.bytestournament.annotations.SwaggerApiResponses;
import com.equipo2.bytestournament.enums.ApiResponse;
import com.equipo2.bytestournament.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * MatchController es un controlador REST que maneja las solicitudes relacionadas con los partidos de cada torneo.
 * 
 * {@link Tag} Anotación que proporciona información adicional para la documentación de Swagger.
 * {@link SwaggerApiResponses} Anotación que proporciona información adicional para la documentación de Swagger.
 * {@link Operation} Anotación que describe la operación de un endpoint específico.
 * 
 * {@link RestController} Anotación que indica que esta clase es un controlador REST.
 * {@link RequestMapping} ("/admin") Define la ruta base para todas las solicitudes manejadas por este controlador.
 * {@link PreAuthorize} Anotación que especifica que ciertos métodos solo pueden ser accedidos por usuarios con roles específicos.
 * 
 * {@link GetMapping} Bean de Spring que define un método que maneja las solicitudes GET
 * {@link PostMapping} Bean de Spring que define un método que maneja las solicitudes POST
 * {@link DeleteMapping} Bean de Spring que define un método que maneja las solicitudes DELETE
 * {@link PutMapping} Bean de Spring que define un método que maneja las solicitudes PUT
 */
@Tag(name = "Match", description = "Controlador para la gestión de partidos en torneos")
@RestController
@RequestMapping("/api/matches")
public class MatchController {
    
    /**
     * MatchService es un servicio que maneja la lógica de negocio relacionada con los partidos.
     */
    private final MatchService matchService;

    public MatchController(MatchService matchService){
        this.matchService = matchService;
    }

    /**
     * Genera partidos para un torneo específico.
     * Este método es accesible solo para usuarios con el rol de ADMIN.
     * 
     * @param tournamentId ID del torneo para el cual se generarán los partidos.
     * @return ResponseEntity<List<MatchDTO>> que contiene una lista de partidos generados y un estado HTTP 201 Created.
     */
    @SwaggerApiResponses
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/generate/{tournamentId}")
    @Operation(summary = "Generar partidos para un torneo", description = "Este endpoint permite a los administradores generar partidos para un torneo específico.")
    public ResponseEntity<List<MatchDTO>> generateMatches(@PathVariable Long tournamentId) {
        return ResponseEntity.status(ApiResponse.CREATED.getStatus()).body(matchService.generateMatches(tournamentId));
    }
    
    /**
     * Comprueba un match por su ID pasado como parámetro.
     * Este método es accesible para todos los usuarios.
     * 
     * @param matchId ID del partido que se desea comprobar.
     * @return MatchDTO que contiene la información del partido.
     */
    @SwaggerApiResponses
    @GetMapping("/{matchId}")
    @Operation(summary = "Comprobar un partido por ID", description = "Este endpoint permite a los usuarios comprobar un partido específico por su ID.")
    public MatchDTO checkMatch(@PathVariable Long matchId) {
        return matchService.checkMatch(matchId);
    }

    /**
     * Actualiza el resultado de un partido.
     * Este método es accesible solo para usuarios con el rol de ADMIN.
     * 
     * @param matchId ID del partido que se desea actualizar.
     * @param macthDTO DTO que contiene la información actualizada del partido, incluyendo el resultado.
     * @return MatchDTO que contiene la información actualizada del partido.
     */
    @SwaggerApiResponses
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/{id}/result")
    @Operation(summary = "Actualizar el resultado de un partido", description = "Este endpoint permite a los administradores actualizar el resultado de un partido específico.")
    public MatchDTO updateMatchResult(@PathVariable Long matchId, @RequestBody MatchDTO macthDTO) {
        return matchService.updateMatchResult(matchId, macthDTO);
    }

}
