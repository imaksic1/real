package com.example.real.dto.user;

import lombok.Data;

@Data
public class UserRequest {
    private String image;
    private String username;
    private String bio;
    private String email;
}
