package com.equipo2.bytestournament.model;
import com.equipo2.bytestournament.enums.Role;

import jakarta.persistence.*;

/**
 * Entidad JPA que representa un usuario de la aplicación de torneos.
 *
 * Contiene el nombre de usuario, el email, la contraseña, el rol, el ranking y los puntos de cada usuario.
 *
 * @author Christian Escalas
*/

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    /**
     * Identificador único del usuario que se genera automaticamente
     */
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    /**
     * Nombre de usuario. Debe ser único y no nulo.
     */
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    /**
     * Dirección de email del usuario. Debe ser única y no nula.
     */
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * Contraseña del usuario. No debe ser nula ni vacía.
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * Rol asignado al usuario. No debe ser nulo ni vacío.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    /**
     * Categoría del usuario. No puede ser nula
     */
    @Column(name = "rank", nullable = false)
    private String rank;

    /**
     * Puntos del usuario. Valor no negativo.
     */
    @Column(name = "points", nullable = false)
    private Integer points;

    protected User() {
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

        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.rank = rank;
        this.points = 0;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {

        if (username.trim().equalsIgnoreCase(this.username)) {
            throw new IllegalArgumentException("No puedes repetir el mismo nombre de usuario.");
        } else {
            this.username = username;
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {

        if (email.trim().equalsIgnoreCase(this.email)) {
            throw new IllegalArgumentException("No puedes repetir el mismo email.");
        } else {
            this.email = email;
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {

        if (password.trim().equalsIgnoreCase(this.password)) {
            throw new IllegalArgumentException("No puedes repetir la misma contraseña.");
        } else {
            this.password = password;
        }
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {

        if (role == this.role) {
            throw new IllegalArgumentException("Rol ya asignado a este usuario.");
        } else {
            this.role = role;
        }
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {

        if (points < 0) {
            throw new IllegalArgumentException("Los puntos no pueden ser negativos.");
        } else {
            this.points = points;
        }
    }
    
}