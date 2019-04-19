package com.example.real.dto.articles;

import lombok.Data;

@Data
public class AuthorDTO {
    private String username;
    private String bio;
    private String image;
    private boolean following;
}
