package com.example.real.service;

import com.example.real.dto.MyResponseBody;
import com.example.real.dto.articles.ArticlesDTO;
import com.example.real.dto.articles.ArticlesHeaderDTO;
import com.example.real.dto.articles.ArticlesInput;
import com.example.real.dto.articles.ArticlesInputHeader;
import com.example.real.dto.error.ErrorArticles;
import com.example.real.dto.error.ErrorArticlesText;
import com.example.real.mapping.ArticlesMapper;
import com.example.real.model.Articles;
import com.example.real.model.UserData;
import com.example.real.repository.ArticlesRepository;
import com.example.real.repository.UserLoginRegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;

@Service
public class ArticlesService {

    @Autowired
    HelpfulService helpfulService;

    @Autowired
    TokenService tokenService;

    @Autowired
    UserLoginRegisterRepository userLoginRegisterRepo;

    @Autowired
    ArticlesRepository articlesRepository;

    @Autowired
    ArticlesMapper articlesMapper;

    public MyResponseBody deleteArticles(HttpHeaders headers,String slug){
        MyResponseBody myResponseBody = new MyResponseBody();
        LinkedList<Articles> articlesList = new LinkedList<>();
        articlesList = articlesRepository.findBySlug(slug);
        if (articlesList.size() != 1){
            return myResponseBody;
        }
        Long id = articlesList.getFirst().getId();
        articlesRepository.deleteById(id);
        return myResponseBody;
    }

    public ResponseEntity articlesInput(ArticlesInputHeader articlesInputHeader, HttpHeaders headers,String slug,boolean post){
        ArticlesInput articlesInput = articlesInputHeader.getArticle();
        ErrorArticlesText errorArticlesText = new ErrorArticlesText();
        ErrorArticles errorArticles = new ErrorArticles();
        boolean hasError = false;
        LinkedList err;
        err = errTitle(articlesInput);
        if (!err.isEmpty()){
            errorArticles.setTitle(err);
            hasError = true;
        }
        err = errBody(articlesInput);
        if (!err.isEmpty()){
            errorArticles.setBody(err);
            hasError = true;
        }
        err = errDescription(articlesInput);
        if (!err.isEmpty()){
            errorArticles.setDescription(err);
            hasError = true;
        }
        if (hasError){
            if (!post){
                String token = tokenService.getTokenStringFromHeader (headers);
                return articlesResponse(token,slug);
            }
            errorArticlesText.setErrors(errorArticles);
            return new ResponseEntity<>(errorArticlesText, HttpStatus.UNPROCESSABLE_ENTITY);
        }else{
            if (post) {
                return saveArticle(articlesInputHeader, headers);
            }else{
                return putArticle(articlesInputHeader, headers,slug);
            }
        }
    }

    LinkedList<String> errTitle (ArticlesInput articlesInput) {
        LinkedList<String> err = new LinkedList();
        if (articlesInput.getTitle() == null || articlesInput.getTitle().length() == 0) {
            err.add("can't be blank");
            err.add("is too short (minimum is 1 character)");
        }
        return err;
    }

    LinkedList<String> errBody (ArticlesInput articlesInput) {
        LinkedList<String> err = new LinkedList();
        if (articlesInput.getBody() == null || articlesInput.getBody().length() == 0) {
            err.add("can't be blank");
            return err;
        }
        return err;
    }

    LinkedList<String> errDescription (ArticlesInput articlesInput) {
        LinkedList<String> err = new LinkedList();
        if (articlesInput.getDescription() == null || articlesInput.getDescription().length() == 0) {
            err.add("can't be blank");
            err.add("is too short (minimum is 1 character)");
        }
        return err;
    }

    private ResponseEntity putArticle(ArticlesInputHeader articlesInputHeader, HttpHeaders headers,String slug){
        ArticlesInput articlesInput = new ArticlesInput();
        articlesInput = articlesInputHeader.getArticle();
        String token = tokenService.getTokenStringFromHeader (headers);
        LinkedList<Articles> articlesList = new LinkedList<>();
        articlesList = articlesRepository.findBySlug(slug);
        if (articlesList.size() != 1) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Articles articles = new Articles();
        articles = articlesList.getFirst();
        articles.setTitle(articlesInput.getTitle());
        articles.setDescription(articlesInput.getDescription());
        articles.setBody(articlesInput.getBody());
        helpfulService.deleteTags(articles);
        articles.setTagList(articlesInput.getTagList());
        Date insertDate = new Date();
        articles.setUpdatedAt(insertDate);
        articlesRepository.save(articles);
        helpfulService.saveTags(articles);
        return articlesResponse(token,slug);
    }

    private ResponseEntity saveArticle(ArticlesInputHeader articlesInputHeader, HttpHeaders headers){
        ArticlesInput articlesInput = new ArticlesInput();
        articlesInput = articlesInputHeader.getArticle();
        String token = tokenService.getTokenStringFromHeader (headers);
        UserData userData;
        userData = userLoginRegisterRepo.findByToken(token);
        Articles articles = new Articles();
        articles.setTitle(articlesInput.getTitle());
        articles.setDescription(articlesInput.getDescription());
        articles.setBody(articlesInput.getBody());
        articles.setTagList(articlesInput.getTagList());
        Date insertDate = new Date();
        articles.setCreatedAt(insertDate);
        articles.setUpdatedAt(insertDate);
        articles.setAuthor(userData);
        String slug = helpfulService.createSlug(articlesInput.getTitle());
        articles.setSlug(slug);
        articles.setFavorited(false);
        articlesRepository.save(articles);
        LinkedList<Articles> articlesList = articlesRepository.findBySlug(slug);
        articles = articlesList.getFirst();
        helpfulService.saveTags(articles);
        return articlesResponse(token,slug);
    }

    private ResponseEntity articlesResponse(String token,String slug){
        LinkedList<Articles> articlesList = new LinkedList<>();
        articlesList = articlesRepository.findBySlug(slug);
        if (articlesList.size() == 1){
            Articles articles = articlesList.getFirst();
            ArticlesDTO articlesDTO = new ArticlesDTO();
            articlesDTO = articlesMapper.articlesToArticlesDTO(articles);
            ArticlesHeaderDTO articlesHeaderDTO = new ArticlesHeaderDTO();
            articlesHeaderDTO.setArticle(articlesDTO);
            return new ResponseEntity<>(articlesHeaderDTO, HttpStatus.OK);
        }else {
            for (int i = 0; i < articlesList.size(); i++){
                if(token.equals(articlesList.get(i).getAuthor().getToken())){
                    Articles articles = articlesList.get(i);
                    ArticlesDTO articlesDTO = new ArticlesDTO();
                    articlesDTO = articlesMapper.articlesToArticlesDTO(articles);
                    ArticlesHeaderDTO articlesHeaderDTO = new ArticlesHeaderDTO();
                    articlesHeaderDTO.setArticle(articlesDTO);
                    return new ResponseEntity<>(articlesHeaderDTO, HttpStatus.OK);
                }
            }
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseEntity returnArticleFromSlug(HttpHeaders headers, String slug){
        String token = tokenService.getTokenStringFromHeader (headers);
        return articlesResponse(token, slug);
    }
}