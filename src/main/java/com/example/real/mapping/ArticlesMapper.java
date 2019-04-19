package com.example.real.mapping;

import com.example.real.dto.articles.ArticlesDTO;
import com.example.real.model.Articles;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.LinkedList;


@Mapper(componentModel = "spring")
public interface ArticlesMapper {

    @Mappings({
            @Mapping(source = "title", target = "title"),
            @Mapping(source = "slug", target = "slug"),
            @Mapping(source = "body", target = "body"),
            @Mapping(source = "createdAt", target = "createdAt"),
            @Mapping(source = "updatedAt", target = "updatedAt"),
            @Mapping(source = "tagList", target = "tagList"),
            @Mapping(source = "description", target = "description"),
            @Mapping(source = "author.username", target = "author.username"),
            @Mapping(source = "author.bio", target = "author.bio"),
            @Mapping(source = "author.image", target = "author.image"),
            @Mapping(source = "author.following", target = "author.following"),
            @Mapping(source = "favorited", target = "favorited"),
            @Mapping(source = "favoritesCount", target = "favoritesCount")
    })
    ArticlesDTO articlesToArticlesDTO(Articles articles);

    LinkedList <ArticlesDTO> articlesToArticlesDTO (LinkedList<Articles> articles);
}
