package com.example.real.service;

import com.example.real.dto.articles.ArticlesDTO;
import com.example.real.dto.articles.ArticlesHeader;
import com.example.real.dto.articles.ArticlesHeaderDTO;
import com.example.real.mapping.ArticlesMapper;
import com.example.real.model.Articles;
import com.example.real.model.Favorited;
import com.example.real.model.Following;
import com.example.real.model.Tags;
import com.example.real.model.UserData;
import com.example.real.repository.ArticlesRepository;
import com.example.real.repository.FavoritedRepository;
import com.example.real.repository.FollowingRepository;
import com.example.real.repository.TagsRepository;
import com.example.real.repository.UserLoginRegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedList;


@Service
public class ArticlesPlusService {

    @Autowired
    TokenService tokenService;

    @Autowired
    HelpfulService helpfulService;

    @Autowired
    ArticlesRepository articlesRepository;

    @Autowired
    FavoritedRepository favoritedRepository;

    @Autowired
    FollowingRepository followingRepository;

    @Autowired
    TagsRepository tagsRepository;

    @Autowired
    UserLoginRegisterRepository userLoginRegisterRepo;

    @Autowired
    ArticlesMapper articlesMapper;

    public ResponseEntity returnArticlesFollowing(HttpHeaders headers, Integer limit,Integer offset){
        String token = tokenService.getTokenStringFromHeader (headers);
        UserData user = userLoginRegisterRepo.findByToken(token);
        LinkedList<Following> userList = followingRepository.findByUserOrderByIdDesc(user);
        LinkedList<Articles> articles = new LinkedList<>();
        for (int i = 0; i < userList.size(); i++){
            articles = helpfulService.addListToArticle(articles,articlesRepository.findByAuthorOrderByIdDesc(userList.get(i).getFollowingUser()));
        }
        return articlesListResponse(user, articles, limit, offset);
    }

    public ResponseEntity deleteFavorite(HttpHeaders headers, String slug){
        LinkedList<Articles> articlesList = articlesRepository.findBySlugOrderByIdAsc(slug);
        if (articlesList.size() != 1){
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Articles articles = articlesList.getFirst();
        articles.setFavoritesCount(articles.getFavoritesCount()-1);
        articlesRepository.save(articles);
        articles.setFavorited(false);
        String token = tokenService.getTokenStringFromHeader (headers);
        if (token == ""){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        UserData user = userLoginRegisterRepo.findByToken(token);
        favoritedRepository.deleteByUserAndArticle(user,articles);
        ArticlesDTO articlesDTO = articlesMapper.articlesToArticlesDTO(articles);
        ArticlesHeaderDTO articlesHeaderDTO = new ArticlesHeaderDTO();
        articlesHeaderDTO.setArticle(articlesDTO);
        return new ResponseEntity<>(articlesHeaderDTO, HttpStatus.OK);
    }

    public ResponseEntity addFavorite(HttpHeaders headers, String slug) {
        LinkedList<Articles> articlesList = articlesRepository.findBySlugOrderByIdAsc(slug);
        if (articlesList.size() != 1) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Articles articles = articlesList.getFirst();
        articles.setFavoritesCount(articles.getFavoritesCount() + 1);
        articlesRepository.save(articles);
        String token = tokenService.getTokenStringFromHeader(headers);
        if (token == ""){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        UserData user = userLoginRegisterRepo.findByToken(token);
        Favorited favorited = new Favorited();
        favorited.setArticle(articles);
        favorited.setUser(user);
        favoritedRepository.save(favorited);
        articles.setFavorited(true);
        ArticlesDTO articlesDTO = articlesMapper.articlesToArticlesDTO(articles);
        ArticlesHeaderDTO articlesHeaderDTO = new ArticlesHeaderDTO();
        articlesHeaderDTO.setArticle(articlesDTO);
        return new ResponseEntity<>(articlesHeaderDTO, HttpStatus.OK);
    }

    public ResponseEntity returnArticlesAuthorFavorited(HttpHeaders headers, String author,String favorited,String tag, Integer limit, Integer offset) {
        UserData userData;
        String token = tokenService.getTokenStringFromHeader(headers);
        userData = userLoginRegisterRepo.findByToken(token);
        LinkedList<Articles> articles = new LinkedList<>();
        if (!(author == null || author.length() == 0)) {
            UserData userdata = new UserData();
            userdata = userLoginRegisterRepo.findByUsername(author);
            articles = articlesRepository.findByAuthorOrderByIdDesc(userdata);
            return articlesListResponse(userData, articles, limit, offset);
        }
        if (!(favorited == null || favorited.length() == 0)) {
            UserData user = userLoginRegisterRepo.findByUsername(favorited);
            LinkedList <Favorited> favoritedArticles = favoritedRepository.findByUserOrderByIdDesc(user);
            for (int i = 0; i < favoritedArticles.size(); i++){
                articles.add(favoritedArticles.get(i).getArticle());
            }
            return articlesListResponse(userData, articles, limit, offset);
        }
        if (!(tag == null || tag.length() == 0)) {
            LinkedList<Tags> tagsList = tagsRepository.findByTagOrderByIdDesc(tag);
            for (int i = 0; i < tagsList.size(); i++){
                articles.add(tagsList.get(i).getArticles());
            }
            return articlesListResponse(userData, articles, limit, offset);
        }
        articles = articlesRepository.findAllByOrderByIdDesc();
        return articlesListResponse(userData, articles, limit, offset);
    }

    public ResponseEntity articlesListResponse(UserData userData, LinkedList <Articles> articles, Integer limit, Integer offset){
        if (userData != null){
            articles = setFavorited (userData,articles);
        }
        Integer articlesSize = articles.size();
        if (articlesSize <= limit){
            return prepareArticleList(articles, articlesSize);
        }else{
            LinkedList<Articles> temp = new LinkedList<>();
            for (int i = offset; i <= offset+limit-1 && i < articlesSize; i++){
                temp.add(articles.get(i));
            }
            return prepareArticleList(temp,articlesSize);
        }
    }
    private LinkedList <Articles> setFavorited (UserData userData, LinkedList <Articles> articles){
        for (int i = 0; i < articles.size(); i++){
            Favorited favorited = favoritedRepository.findByUserAndArticle(userData,articles.get(i));
            if (favorited == null){
                articles.get(i).setFavorited(false);
            }else{
                articles.get(i).setFavorited(true);
            }
        }
        return articles;
    }
    private ResponseEntity prepareArticleList(LinkedList  articles, Integer articlesSize){
        LinkedList<ArticlesDTO> articlesDTO = articlesMapper.articlesToArticlesDTO(articles);
        ArticlesHeader articlesHeader = new ArticlesHeader();
        articlesHeader.setArticles(articlesDTO);
        articlesHeader.setArticlesCount(articlesSize);
        return new ResponseEntity<>(articlesHeader, HttpStatus.OK);
    }

}
