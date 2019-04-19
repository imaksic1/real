package com.example.real.dto.login;

import com.example.real.dto.user.UserDataDTO;
import lombok.Data;

@Data
public class LoginUserResponse {
    private UserDataDTO user;
}
