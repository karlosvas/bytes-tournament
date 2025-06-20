package com.equipo2.bytestournament.mapper;

import com.equipo2.bytestournament.DTO.UserDTO;
import com.equipo2.bytestournament.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    
    public UserDTO userToUserDTO(User user) {
       return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .rank(user.getRank())
                .points(user.getPoints())
                .build();
    }
    
    public User userDTOToUser(UserDTO dto) {
       return User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(dto.getRole())
                .rank(dto.getRank())
                .points(dto.getPoints())
                .build();
    }
}