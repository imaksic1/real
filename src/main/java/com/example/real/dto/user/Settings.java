package com.example.real.dto.user;

import lombok.Data;

import java.util.Date;

@Data
public class Settings {
    private String email;
    private String username;
    private String bio;
    private String image;
    private String password;
}
