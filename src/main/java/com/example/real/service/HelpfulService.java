package com.example.real.service;

import com.example.real.model.Articles;
import com.example.real.model.Tags;
import com.example.real.repository.TagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

@Service
public class HelpfulService {

    @Autowired
    TagsRepository tagsRepository;

    public void deleteTags(Articles articles){
        Tags tags = new Tags();
        if (articles.getTagList().isEmpty()){
            return;
        }
        for (int i = 0; i < articles.getTagList().size(); i++){
            tagsRepository.deleteByArticlesAndTag(articles,articles.getTagList().get(i));
        }
    }

    public void saveTags(Articles articles){
        if (articles.getTagList().isEmpty()){
            return;
        }
        for (int i = 0; i < articles.getTagList().size(); i++){
            Tags tags = new Tags();
            tags.setArticles(articles);
            tags.setTag(articles.getTagList().get(i));
            tagsRepository.save(tags);
        }
    }

    public LinkedList addListToArticle(LinkedList articles,LinkedList plus){
        for(int i = 0; i < plus.size(); i++){
            articles.add(plus.get(i));
        }
        return articles;
    }

    public String createSlug (String slug){
        slug = slug +"-";
        slug = slug.toLowerCase();
        slug = changeBlankToMinus(slug);
        slug += randSlug(6);
        return slug;
    }

    private String changeBlankToMinus(String slug){
        String mySlug = "";
        for (int i = 0; i < slug.length(); i++){
            if (slug.substring(i,i+1).equals(" ")){
                mySlug += "-";
            }else{
                mySlug += slug.substring(i,i+1);
            }
        }
        return mySlug;
    }

    private String randSlug(Integer len){
        String output = "";
        for (int i = 0; i < len; i++) {
            Integer selector = (int) (Math.random() * 10);
            if (selector == 1){
                output += randNumber();
            }else{
                output += randString();
            }
        }
        return output;
    }

    private String randNumber(){
        int selector = (int)( Math.random() * 10);
        return Integer.toString(selector);
    }

    private String randString(){
        String output;
        int selector = (int) (Math.random() * 26);
        char start = (char)('a' + selector);
        output = String.valueOf(start);
        return output;
    }
}
