package com.example.real.controller;

import com.example.real.dto.comments.CommentsEmptyResponse;
import com.example.real.dto.comments.CommentsRequestHeader;
import com.example.real.service.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentsController {

    @Autowired
    CommentsService commentsService;

    @GetMapping("/api/articles/{slug}/comments")
    public ResponseEntity getComments(@RequestHeader HttpHeaders headers, @PathVariable("slug") String slug) {
        return commentsService.getCommentList(headers,slug);
    }

    @PostMapping("/api/articles/{slug}/comments")
    public ResponseEntity postComments(@RequestHeader HttpHeaders headers, @PathVariable("slug") String slug,
                                          @RequestBody CommentsRequestHeader commentsRequestHeader) {
        return commentsService.manageComments(headers, slug, commentsRequestHeader);
    }

    @DeleteMapping("/api/articles/{slug}/comments/{comment}")
    public CommentsEmptyResponse delComment(@PathVariable("slug") String slug, @PathVariable("comment") Long comment) {
        return commentsService.deleteComment(comment);
    }
}
