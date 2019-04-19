package com.example.real.dto.user;

import lombok.Data;

@Data
public class UserResponse {
    private String image;
    private String username;
    private String bio;
    private boolean following;
}
