package com.example.real.dto.articles;

import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;

@Data
public class ArticlesDTO {
    private String title;
    private String slug;
    private String body;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    private ArrayList<String> tagList;
    private String description;
    private AuthorDTO author;
    private boolean favorited;
    private int favoritesCount;
}
