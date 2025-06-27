package com.equipo2.bytestournament.contoller;

import org.springframework.web.bind.annotation.RestController;
import com.equipo2.bytestournament.DTO.TournamentDTO;
import com.equipo2.bytestournament.annotations.SwaggerApiResponses;
import com.equipo2.bytestournament.enums.ApiResponse;
import com.equipo2.bytestournament.service.TournamentService;
import com.equipo2.bytestournament.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * TournamentController es un controlador REST que maneja las solicitudes
 * relacionadas con los torneos.
 * Este controlador permite a los administradores crear torneos, permitir que
 * los jugadores se unan a ellos,
 * obtener clasificaciones y detalles de los torneos.
 * 
 * 
 * {@link Tag} Anotación que proporciona información adicional para la
 * documentación de Swagger.
 * {@link SwaggerApiResponses} Anotación que proporciona información adicional
 * para la documentación de Swagger.
 * {@link Operation} Anotación que describe la operación de un endpoint
 * específico.
 * 
 * {@link RestController} Anotación que indica que esta clase es un controlador
 * REST.
 * {@link RequestMapping} ("/admin") Define la ruta base para todas las
 * solicitudes manejadas por este controlador.
 * {@link PreAuthorize} Anotación que especifica que ciertos métodos solo pueden
 * ser accedidos por usuarios con roles específicos.
 * 
 * {@link GetMapping} Bean de Spring que define un método que maneja las
 * solicitudes GET
 * {@link PostMapping} Bean de Spring que define un método que maneja las
 * solicitudes POST
 * {@link DeleteMapping} Bean de Spring que define un método que maneja las
 * solicitudes DELETE
 * {@link PutMapping} Bean de Spring que define un método que maneja las
 * solicitudes PUT
 */
@Tag(name = "Tournament", description = "Controlador para la gestión de torneos")
@RestController
@RequestMapping("/api/tournaments")
public class TournamentController {

    /**
     * TournamentService es un servicio que maneja la lógica de negocio relacionada
     * con los torneos.
     * UserService es un servicio que maneja la lógica de negocio relacionada con
     * los usuarios.
     */
    private final TournamentService tournamentService;
    private final UserService userService;

    public TournamentController(TournamentService tournamentService, UserService userService) {
        this.tournamentService = tournamentService;
        this.userService = userService;
    }

    /**
     * Crea un nuevo torneo.
     * Este método es accesible solo para usuarios con el rol de ADMIN.
     * 
     * @param tournamentDTO DTO que contiene la información del torneo a crear.
     * @return ResponseEntity<TournamentDTO> que contiene el torneo creado y un
     *         estado HTTP 201 Created.
     */
    @SwaggerApiResponses
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Crear un torneo", description = "Este endpoint permite a los administradores crear un nuevo torneo.")
    public ResponseEntity<TournamentDTO> create(@RequestBody TournamentDTO tournamentDTO) {
        return ResponseEntity.status(ApiResponse.CREATED.getStatus())
                .body(tournamentService.createTournament(tournamentDTO));
    }

    /**
     * Nos unimos a un torneo existente
     * Este método es accesible para usuarios con el rol de ADMIN y PLAYER.
     * 
     * @param tournamentId ID del torneo a actualizar.
     * @param userName     Nombre del usuario que está actualizando el torneo.
     * @return TournamentDTO que contiene el torneo actualizado y un estado HTTP 200 OK.
     */
    @SwaggerApiResponses
    // TODO: Si eres player solo puedes unirte tu mismo
    @PreAuthorize("hasRole('PLAYER')")
    @PreAuthorize("hasAnyRole('PLAYER', 'ADMIN')")
    @PostMapping("/players")
    @Operation(summary = "Unirse a un torneo como jugador", description = "Este endpoint permite a los jugadores unirse a un torneo existente.")
    public TournamentDTO joinTournament(@RequestParam Long tournamentId, @RequestParam String userName) {
        return tournamentService.addPlayerToTournament(tournamentId, userName);
    }

    /**
     * Permite al usuario autenticado unirse a un torneo existente.
     * Este método es accesible para todos los usuarios autenticados.
     * Este método utiliza el servicio UserService para obtener el nombre del
     * usuario autenticado.
     * y luego llama al servicio TournamentService para agregar al usuario al torneo
     * especificado.
     * 
     * @param tournamentId ID del torneo al que el usuario desea unirse.
     * @param authentication Objeto de autenticación que contiene la información del usuario autenticado.
     * @return ResponseEntity<TournamentDTO> que contiene el torneo actualizado y un estado HTTP 201 Created.
     */
    @SwaggerApiResponses
    @PreAuthorize("hasAnyRole('PLAYER', 'ADMIN')")
    @PostMapping("/players/me")
    @Operation(summary = "Unirse a un torneo como jugador autenticado", description = "Este endpoint permite al usuario autenticado unirse a un torneo existente.")
    public ResponseEntity<TournamentDTO> joinTournamentWithActualUser(@RequestParam Long tournamentId,
            Authentication authentication) {
        String name = userService.getUserNameFronAutentication(authentication);
        return ResponseEntity.status(ApiResponse.CREATED.getStatus())
                .body(tournamentService.addPlayerToTournament(tournamentId, name));
    }

    /**
     * Obtiene la clasificación de un torneo específico.
     * Este método es accesible para todos los usuarios.
     * Obtiene la clasificación en formato JSON, o en el caso de que el parámetro
     * pretty sea true,
     * en formato String para una visualización más amigable.
     * 
     * @param tournamentId ID del torneo del cual se desea obtener la clasificación.
     * @param pretty Indica si la respuesta debe ser formateada de manera amigable (pretty) o no.
     * @return ResponseEntity<?> que contiene la clasificación del torneo y un estado HTTP 200 OK.
     */
    @SwaggerApiResponses
    @GetMapping("/ranking/{tournamentId}")
    @Operation(summary = "Obtener la clasificación de un torneo", description = "Este endpoint permite obtener la clasificación de un torneo específico.")
    public ResponseEntity<?> getClassification(@PathVariable Long tournamentId,
            @RequestParam(value = "pretty", required = false) boolean pretty) {
        var ranking = tournamentService.getClassification(tournamentId);
        return ResponseEntity.ok(pretty ? ranking.toString() : ranking);
    }

    /**
     * Obtiene los detalles del ranking de un torneo específico.
     * Este método es accesible para todos los usuarios.
     * Obtiene los detalles del ranking
     * de un torneo específico por su ID en formato JSON, o en el caso de que el
     * parámetro pretty sea true,
     * en formato String para una visualización más amigable.
     * 
     * @param tournamentId
     * @param pretty
     * @return
     */
    @SwaggerApiResponses
    @GetMapping("/ranking/details/{tournamentId}")
    @Operation(summary = "Obtener detalles del ranking de un torneo", description = "Este endpoint permite obtener los detalles del ranking de un torneo específico.")
    public ResponseEntity<?> getRankingDetails(@PathVariable Long tournamentId,
            @RequestParam(value = "pretty", required = false) boolean pretty) {
        var ranking = tournamentService.getRankingDetails(tournamentId);
        return ResponseEntity.ok((pretty) ? ranking.toString() : ranking);
    }
    
    @SwaggerApiResponses
    @PutMapping
    @Operation(summary = "Actualizar un torneo", description = "Este endpoint permite a los administradores actualizar un torneo existente.")
    public TournamentDTO updateTournament(@RequestBody TournamentDTO tournamentDTO, Authentication authentication) {
        return tournamentService.updateTournament(tournamentDTO, authentication);
    }

    /**
     * Lista todos los torneos disponibles.
     * Este método es accesible para todos los usuarios.
     * 
     * @return List<TournamentDTO> que contiene todos los torneos.
     */
    @SwaggerApiResponses
    @GetMapping
    @Operation(summary = "Listar torneos", description = "Este endpoint permite listar los torneos.")
    public List<TournamentDTO> getTournaments() {
        return tournamentService.getTournament();
    }

    /**
     * Obtiene los detalles de un torneo específico por su ID.
     * Este método es accesible para todos los usuarios.
     * 
     * @param id ID del torneo a obtener.
     * @return TournamentDTO que contiene el torneo encontrado y un  estado HTTP 200 OK.
     */
    @SwaggerApiResponses
    @GetMapping("/{id}")
    @Operation(summary = "Detalles del torneo", description = "Este endpoint muestra los detalles de un torneo por su ID.")
    public TournamentDTO getTournamentById(@PathVariable Long id) {
        return tournamentService.findTournamentById(id);
    }


}
