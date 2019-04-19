package com.example.real.dto.articles;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ArticlesInput {
    private String title;
    private String description;
    private String body;
    private ArrayList<String> tagList;
}
