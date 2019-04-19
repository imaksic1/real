package com.example.real.service;

import com.example.real.dto.comments.CommentsEmptyResponse;
import com.example.real.dto.comments.CommentsListResponse;
import com.example.real.dto.comments.CommentsRequest;
import com.example.real.dto.comments.CommentsRequestHeader;
import com.example.real.dto.comments.CommentsResponse;
import com.example.real.dto.comments.CommentsResponseHeader;
import com.example.real.mapping.CommentsMapper;
import com.example.real.model.Articles;
import com.example.real.model.Comments;
import com.example.real.model.UserData;
import com.example.real.repository.ArticlesRepository;
import com.example.real.repository.CommentsRepository;
import com.example.real.repository.UserLoginRegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;

@Service
public class CommentsService {

    @Autowired
    TokenService tokenService;

    @Autowired
    CommentsRepository commentsRepository;

    @Autowired
    ArticlesRepository articlesRepository;

    @Autowired
    UserLoginRegisterRepository userLoginRegisterRepo;


    @Autowired
    CommentsMapper commentsMapper;

    public CommentsEmptyResponse deleteComment(Long comment){
        commentsRepository.deleteById(comment);
        return new CommentsEmptyResponse();
    }

    public ResponseEntity manageComments (HttpHeaders headers, String slug, CommentsRequestHeader commentsRequestHeader){
        String token = tokenService.getTokenStringFromHeader (headers);
        saveComment(token,slug,commentsRequestHeader);
        return responseComment(token,slug,commentsRequestHeader);
    }

    private ResponseEntity responseComment (String token,String slug,CommentsRequestHeader commentsRequestHeader){
        String body = extractBody(commentsRequestHeader);
        LinkedList<Articles> articles = articlesRepository.findBySlug(slug);
        if (articles.size() != 1) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        LinkedList<Comments> response = commentsRepository.findByBody(body);
        Comments comments = response.getFirst();
        for (int i = 0; i < response.size(); i++){
            if (response.get(i).getArticles().equals(articles.getFirst()) && response.get(i).getAuthor().getToken().equals(token)){
                comments = response.get(i);
                break;
            }
        }
        CommentsResponse commentsResponse = commentsMapper.commentsToCommentsResponse(comments);
        CommentsResponseHeader commentsResponseHeader = new CommentsResponseHeader();
        commentsResponseHeader.setComment(commentsResponse);
        return new ResponseEntity<>(commentsResponseHeader, HttpStatus.OK);
    }

    private String extractBody(CommentsRequestHeader commentsRequestHeader){
        CommentsRequest commentsRequest = new CommentsRequest();
        commentsRequest = commentsRequestHeader.getComment();
        return commentsRequest.getBody();
    }

    private void saveComment(String token, String slug,CommentsRequestHeader commentsRequestHeader){
        String body = extractBody(commentsRequestHeader);
        Comments comments = new Comments();
        comments.setBody(body);
        Date inputDate = new Date();
        comments.setCreatedAt(inputDate);
        comments.setUpdatedAt(inputDate);
        UserData userData = userLoginRegisterRepo.findByToken(token);
        comments.setAuthor(userData);
        LinkedList<Articles> articles = new LinkedList<>();
        articles = articlesRepository.findBySlug(slug);
        if (articles.size() == 1){
            comments.setArticles(articles.getFirst());
        }else{
            for (int i = 0; i < articles.size(); i++){
                if (articles.get(i).getAuthor().getToken().equals(token)){
                    comments.setArticles(articles.get(i));
                    break;
                }
            }
        }
        commentsRepository.save(comments);
    }

    public ResponseEntity<?> getCommentList(HttpHeaders headers,String slug){
        LinkedList <Articles> articles = new LinkedList <>();
        articles = articlesRepository.findBySlug(slug);
        if (articles.size() != 1) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Articles slugArticle = articles.getFirst();
        LinkedList<Comments> comments = commentsRepository.findByArticlesOrderByIdDesc(slugArticle);
        LinkedList<CommentsResponse> commentsResponse = commentsMapper.commentsToCommentsResponse(comments);
        CommentsListResponse commentsListResponse = new CommentsListResponse();
        commentsListResponse.setComments(commentsResponse);
        return new ResponseEntity<>(commentsListResponse, HttpStatus.OK);
    }
}
