package com.equipo2.bytestournament.model;

import com.equipo2.bytestournament.enums.Role;
import jakarta.persistence.*;

/**
 * Entidad JPA que representa un usuario de la aplicación de torneos.
 * <p>
 * Contiene el nombre de usuario, el email, la contraseña, el rol, el ranking y los puntos de cada usuario.
 *
 * @author Christian Escalas
 */

@Entity
public class User {

    /**
     * Identificador único del usuario que se genera automaticamente
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    /**
     * Nombre de usuario. Debe ser único y no nulo.
     */
    @Column(name = "username", updatable = true, nullable = false, unique = true)
    private String username;

    /**
     * Dirección de email del usuario. Debe ser única y no nula.
     */
    @Column(name = "email", updatable = true, nullable = false, unique = true)
    private String email;

    /**
     * Contraseña del usuario. No debe ser nula ni vacía.
     */
    @Column(name = "password", updatable = true, nullable = false)
    private String password;

    /**
     * Rol asignado al usuario. No debe ser nulo ni vacío.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", updatable = true, nullable = false)
    private Role role;

    /**
     * Categoría del usuario. No puede ser nula
     */
    @Column(name = "rank", updatable = true, nullable = false)
    private String rank;

    /**
     * Puntos del usuario. Valor no negativo.
     */
    @Column(name = "points", updatable = true, nullable = false)
    private Integer points;

    public User() {
    }

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
    public User(String username, String email, String password, Role role, String rank) {

        if (username == null) {
            throw new IllegalArgumentException("El nombre de usuario no puede ser nulo.");
        }
        if (email == null) {
            throw new IllegalArgumentException("El email no puede ser nulo.");
        }
        if (password == null) {
            throw new IllegalArgumentException("la contraseña no puede ser nula.");
        }
        if (role == null) {
            throw new IllegalArgumentException("El rol no puede ser nulo.");
        }
        if (rank == null) {
            throw new IllegalArgumentException("El ránking no puede ser nulo.");
        }

        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.rank = rank;
        this.points = 0;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException("El nombre de usuario no puede ser nulo.");
        }
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("El email no puede ser nulo.");
        }
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null) {
            throw new IllegalArgumentException("la contraseña no puede ser nula.");
        }
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("El rol no puede ser nulo.");
        }
        this.role = role;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        if (rank == null) {
            throw new IllegalArgumentException("El ránking no puede ser nulo.");
        }
        this.rank = rank;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("Los puntos no pueden ser negativos.");
        }
        this.points = points;
    }
}