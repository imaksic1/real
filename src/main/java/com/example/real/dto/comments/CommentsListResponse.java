package com.example.real.dto.comments;

import lombok.Data;

import java.util.LinkedList;

@Data
public class CommentsListResponse {
    LinkedList<CommentsResponse> comments;
}
