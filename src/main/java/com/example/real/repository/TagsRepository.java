package com.example.real.repository;

import com.example.real.dto.TagsSum;
import com.example.real.model.Articles;
import com.example.real.model.Tags;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.LinkedList;

@Repository
public interface TagsRepository extends CrudRepository<Tags,Long> {

    LinkedList<Tags> findByTagOrderByIdDesc(String tag);

    @Query("SELECT u.tag FROM Tags u GROUP BY tag ORDER BY COUNT(u.tag) DESC")
    LinkedList<String> findTagsSum();

    @Transactional
    void deleteByArticlesAndTag(Articles articles, String tag);
}
