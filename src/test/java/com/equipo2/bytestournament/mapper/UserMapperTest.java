package com.equipo2.bytestournament.mapper;

import com.equipo2.bytestournament.DTO.UserDTO;
import com.equipo2.bytestournament.enums.Rank;
import com.equipo2.bytestournament.enums.Role;
import com.equipo2.bytestournament.mapper.helper.UserMapperHelper;
import com.equipo2.bytestournament.model.User;
import com.equipo2.bytestournament.repository.TournamentRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Field;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
* Clase de prueba para UserMapper.
* Esta clase contiene pruebas unitarias para los métodos de conversión entre User y UserDTO.
* {@link UserMapper} se utiliza para mapear entre las entidades User y UserDTO.
* {@link Test} se utiliza para marcar los métodos de prueba.
 * {@link BeforeAll} se utiliza para ejecutar un método antes de cada prueba, lo que permite configurar el entorno de prueba.
*/
class UserMapperTest {

    private UserMapper userMapper;

    @Mock
    private TournamentRepository tournamentRepository;

    /**
     * setUp Método que se ejecuta antes de cada prueba.
     * Inicializa el UserMapper y su helper, inyectando el TournamentRepository necesario.
     */
    @BeforeEach
    void setUp() {
        try {
            MockitoAnnotations.openMocks(this);
            userMapper = Mappers.getMapper(UserMapper.class);
            UserMapperHelper helper = new UserMapperHelper(tournamentRepository);
            Field helperField = userMapper.getClass().getDeclaredField("userMapperHelper");
            helperField.setAccessible(true);
            helperField.set(userMapper, helper);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }



     /**
      * Test para verificar la conversión de User a UserDTO y viceversa.
      * Se crea un objeto User, se convierte a UserDTO y luego se convierte de nuevo a User.
      * Se comprueba que los valores coincidan en ambas conversiones.
      */
     @Test
     void testUserToUserDTO() {
         User user = User.builder()
                 .id(1L)
                 .username("testUserMapper")
                 .email("testmapper@gmail.com")
                 .password("1234")
                 .role(Role.ADMIN)
                 .rank(Rank.DIAMOND)
                 .points(100)
                 .build();

         UserDTO userDTO = userMapper.userToUserDTO(user);

         // Verificamos que el UserDTO no sea nulo y que los valores coincidan con el User original
         assertNotNull(userDTO);
         assertAll(
                 () -> assertEquals(user.getId(), userDTO.getId()),
                 () -> assertEquals(user.getEmail(), userDTO.getEmail()),
                 () -> assertEquals(user.getUsername(), userDTO.getUsername()),
                 () -> assertEquals(user.getRole(), userDTO.getRole()),
                 () -> assertEquals(user.getRank(), userDTO.getRank()),
                 () -> assertEquals(user.getPoints(), userDTO.getPoints())
         );
     }

     /**
      * Test para verificar la conversión de UserDTO a User.
      * Se crea un objeto UserDTO, se convierte a User y se comprueba que los valores coincidan.
      * Nota: El ID del UserDTO se establece como null porque es la DB la que se encarga de asignar un ID único al crear un nuevo usuario.
      */
     @Test
     void testUserDTOToUser() {
             UserDTO userDTO = UserDTO.builder()
                     .id(1L)  // Ejemplo de ID que proporciono el User
                     .username("testUserMapper")
                     .email("testmapper@gmail.com")
                     .password("1234")
                     .role(Role.ADMIN)
                     .rank(Rank.DIAMOND)
                     .points(100)
                     .build();

             User user = userMapper.userDTOToUser(userDTO);

              // Verificamos que el User no sea nulo y que los valores coincidan con el UserDTO original, a excecpción del ID
              // porque se genera automáticamente al guardar en la base de datos.
             assertNotNull(user);
             assertAll(
                     () -> assertEquals(userDTO.getId(), user.getId()),
                     () -> assertEquals(userDTO.getEmail(), user.getEmail()),
                     () -> assertEquals(userDTO.getUsername(), user.getUsername()),
                     () -> assertEquals(userDTO.getRole(), user.getRole()),
                     () -> assertEquals(userDTO.getRank(), user.getRank()),
                     () -> assertEquals(userDTO.getPoints(), user.getPoints())
             );
     }
}