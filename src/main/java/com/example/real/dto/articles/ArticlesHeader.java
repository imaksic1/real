package com.example.real.dto.articles;

import lombok.Data;

import java.util.List;

@Data
public class ArticlesHeader {
    private List articles;
    private Integer articlesCount;
}
