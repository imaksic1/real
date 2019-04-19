package com.example.real.controller;

import com.example.real.dto.MyResponseBody;
import com.example.real.dto.articles.ArticlesInputHeader;
import com.example.real.service.ArticlesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ArticlesController {

    @Autowired
    ArticlesService articlesService;

    @PostMapping("/api/articles")
    public ResponseEntity postArticles(@RequestHeader HttpHeaders headers, @RequestBody ArticlesInputHeader articlesInputHeader) {
        return articlesService.articlesInput(articlesInputHeader,headers,"",true);
    }

    @PutMapping("/api/articles/{slug}")
    public ResponseEntity putArticles(@RequestHeader HttpHeaders headers, @RequestBody ArticlesInputHeader articlesInputHeader,
                                         @PathVariable("slug") String slug) {
        return articlesService.articlesInput(articlesInputHeader,headers,slug,false);
    }

    @GetMapping("/api/articles/{slug}")
    public ResponseEntity getSlug(@RequestHeader HttpHeaders headers, @PathVariable("slug") String slug) {
        return articlesService.returnArticleFromSlug(headers,slug);
    }

    @DeleteMapping("/api/articles/{slug}")
    public MyResponseBody deleArticles(@RequestHeader HttpHeaders headers, @PathVariable("slug") String slug) {
        return articlesService.deleteArticles(headers,slug);
    }
}
