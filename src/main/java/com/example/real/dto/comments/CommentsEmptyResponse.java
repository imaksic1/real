package com.example.real.dto.comments;

import com.example.real.dto.error.ErrorArticles;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CommentsEmptyResponse {
    private String emptyString;
}
