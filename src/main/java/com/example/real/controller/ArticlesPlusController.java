package com.example.real.controller;

import com.example.real.dto.articles.ArticlesHeader;
import com.example.real.dto.user.UserProfileResponse;
import com.example.real.service.ArticlesPlusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticlesPlusController {

    @Autowired
    ArticlesPlusService articlesPlusService;

    @GetMapping("/api/articles")
    public ResponseEntity getAtriclesAuthor(@RequestHeader HttpHeaders headers,@RequestParam(value = "author", required=false) String author,
                                            @RequestParam(value = "favorited", required=false) String favorited,
                                            @RequestParam(value = "tag", required=false) String tag,
                                            @RequestParam(value = "limit") Integer limit, @RequestParam(value = "offset") Integer offset) {
        return articlesPlusService.returnArticlesAuthorFavorited(headers, author, favorited, tag, limit, offset);
    }

    @GetMapping("/api/articles/feed")
    ResponseEntity articlesFeed(@RequestHeader HttpHeaders headers,@RequestParam("limit") Integer limit,
                                   @RequestParam("offset") Integer offset) {
        return articlesPlusService.returnArticlesFollowing(headers, limit, offset);
    }

    @PostMapping("/api/articles/{slug}/favorite")
    public ResponseEntity postFavorite(@RequestHeader HttpHeaders headers, @PathVariable("slug") String slug) {
        return articlesPlusService.addFavorite(headers, slug);
    }

    @DeleteMapping("/api/articles/{slug}/favorite")
    public ResponseEntity deleteFavorite(@RequestHeader HttpHeaders headers,@PathVariable("slug") String slug) {
        return articlesPlusService.deleteFavorite(headers, slug);
    }
}
