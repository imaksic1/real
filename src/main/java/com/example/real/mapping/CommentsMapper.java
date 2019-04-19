package com.example.real.mapping;

import com.example.real.dto.articles.ArticlesDTO;
import com.example.real.dto.comments.CommentsResponse;
import com.example.real.model.Articles;
import com.example.real.model.Comments;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.LinkedList;

@Mapper(componentModel = "spring")
public interface CommentsMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "createdAt", target = "createdAt"),
            @Mapping(source = "updatedAt", target = "updatedAt"),
            @Mapping(source = "body", target = "body"),
            @Mapping(source = "author.username", target = "author.username"),
            @Mapping(source = "author.bio", target = "author.bio"),
            @Mapping(source = "author.image", target = "author.image"),
            @Mapping(source = "author.following", target = "author.following")
    })
    CommentsResponse commentsToCommentsResponse(Comments comments);

    LinkedList<CommentsResponse> commentsToCommentsResponse(LinkedList<Comments> comments);
}
