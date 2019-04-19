package com.example.real.dto.comments;

import com.example.real.dto.articles.AuthorDTO;
import lombok.Data;

import java.util.Date;

@Data
public class CommentsResponse {
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private String body;
    private AuthorDTO author;
}
