package com.example.real.dto;

import lombok.Data;

@Data
public class LoginUserRequest {
    private LoginUser user;

    public LoginUser getUser() {
        return user;
    }

    public void setUser(LoginUser user) {
        this.user = user;
    }
}
