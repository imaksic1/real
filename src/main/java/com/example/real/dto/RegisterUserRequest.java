package com.example.real.dto;

import lombok.Data;

@Data
public class RegisterUserRequest {
    private RegisterUser user;

    public RegisterUser getUser() {
        return user;
    }

    public void setUser(RegisterUser user) {
        this.user = user;
    }
}
