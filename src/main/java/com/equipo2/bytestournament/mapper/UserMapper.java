package com.equipo2.bytestournament.mapper;

import com.equipo2.bytestournament.DTO.UserDTO;
import com.equipo2.bytestournament.mapper.helper.UserMapperHelper;
import com.equipo2.bytestournament.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {UserMapperHelper.class})
public interface UserMapper {
    public final Logger logger = LoggerFactory.getLogger(UserMapper.class);

    // User -> UserDTO
    @Mapping(source = "tournaments", target = "tournaments")
    @Mapping(source = ".", target = "matches") // Combina las listas de cuando a sido palyer1 y player2
    @Mapping(target = "authorityPrivilegies", source = "authorityPrivilegies")
    @Mapping(target = "password", ignore = true) // Ignoramos la contraseña para que no se mapee desde el DTO
    UserDTO userToUserDTO(User user);
    
    // UserDTO -> User
    // Como en el UserDTO tenemos todos los matches sin diferenciar entre si es player 1 o dos, no podemos agreagr users desde userDto a user
    @Mapping(target = "matchesAsPlayer1", ignore = true)
    @Mapping(target = "matchesAsPlayer2", ignore = true)
    @Mapping(target = "tournaments", source = "tournaments")
    @Mapping(target = "authorityPrivilegies", source = "authorityPrivilegies")
    User userDTOToUser(UserDTO userDTO);

    @AfterMapping
    default void logAfterUserMapping(User user, @MappingTarget UserDTO userDTO) {
        logger.info("Finalizado mapeo de User a UserDTO para usuario: " + userDTO.getUsername());
    }
}