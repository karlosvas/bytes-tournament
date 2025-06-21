package com.equipo2.bytestournament.model;

import com.equipo2.bytestournament.enums.AuthorityPrivilegies;
import com.equipo2.bytestournament.enums.Rank;
import com.equipo2.bytestournament.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


/**
 * Entidad JPA que representa un usuario de la aplicación de torneos.
 * Contiene el nombre de usuario, el email, la contraseña, el rol, el ranking y los puntos de cada usuario.
 *
 * @author Christian Escalas
 */
@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
@Builder
public class User implements UserDetails{

    /**
     * id: Identificador único del usuario que se genera automaticamenteç
     * username: Nombre de usuario único y no nulo.
     * email: Dirección de email del usuario. Debe ser única y no nula.
     * password: Contraseña del usuario. No debe ser nula ni vacía.
     * role: Rol asignado al usuario. No debe ser nulo ni vacío.
     * rank: Categoría del usuario. No puede ser nula
     * points: Puntos del usuario. Valor no negativo.
     * matchesAsPlayer1: Lista de partidas en las que el usuario ha participado como jugador 1.
     * matchesAsPlayer2: Lista de partidas en las que el usuario ha participado como jugador 2.
     * tournaments: Lista de torneos en los que participa el usuario, un usuario puede participar 
     * en múltiples torneos y un torneo puede tener múltiples jugadores.
    * authorityPrivilegies:  Lista de privilegios adicionales asignados al usuario.
    * Estos privilegios se combinan con los del rol del usuario para determinar sus permisos.
    * Se almacenan en una tabla separada para permitir una gestión flexible de los privilegios.
     * Constructor por defecto requerido por JPA.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "username", updatable = true, nullable = false, unique = true)
    private String username;

    @Column(name = "email", updatable = true, nullable = false, unique = true)
    private String email;

    @Column(name = "password", updatable = true, nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", updatable = true, nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "rank", updatable = true, nullable = false)
    private Rank rank;

    @Column(name = "points", updatable = true, nullable = false)
    private Integer points;

    @OneToMany(mappedBy = "player1")
    private List<Match> matchesAsPlayer1 = new ArrayList<>();

    @OneToMany(mappedBy = "player2")
    private List<Match> matchesAsPlayer2 = new ArrayList<>();

    @ManyToMany(mappedBy = "players")
    private List<Tournament> tournaments = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "user_authority_privilegies", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<AuthorityPrivilegies> authorityPrivilegies = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Combina los privilegios del rol con los privilegios adicionales del usuario
        Set<AuthorityPrivilegies> allPrivilegies = new HashSet<>();
        if (authorityPrivilegies != null) {
            allPrivilegies.addAll(authorityPrivilegies);
        }
        
        return allPrivilegies.stream()
                .map(authority -> (GrantedAuthority) () -> authority.name())
                .toList();
    }

    public User() {}

    /**
     * Construye un nuevo usuario con nombre, email, contraseña, rol y rango es opcional.
     * Los puntos iniciales se establecen a 0.
     *
     * @param username nombre de usuario. No puede ser null ni vacío.
     * @param email    dirección de email. No puede ser null ni vacío.
     * @param password contraseña. No puede ser null ni vacío.
     * @param role     rol del usuario. No puede ser null.
     * @param rank     ranking o categoría; puede ser null o vacío.
     */
    public User(String username, String email, String password, Role role, Rank rank) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.rank = rank;
        this.points = 0; 
    }
}