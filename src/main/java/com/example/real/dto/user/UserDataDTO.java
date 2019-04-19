package com.example.real.dto.user;

import lombok.Data;

import java.util.Date;

@Data
public class UserDataDTO {
    private Long id;
    private String email;
    private Date createdAt;
    private Date updatedAt;
    private String username;
    private String bio;
    private String image;
    private String token;
}
