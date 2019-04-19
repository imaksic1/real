package com.example.real.mapping;

import com.example.real.dto.user.UserDataDTO;
import com.example.real.dto.user.UserResponse;
import com.example.real.model.UserData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDataMapper {

    UserDataDTO UserDataToUserDataDTO(UserData userData);

    UserResponse UserDataToUserResponse(UserData userData);
}
