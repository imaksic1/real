package com.example.real.repository;

import com.example.real.model.Articles;
import com.example.real.model.Comments;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.LinkedList;

public interface CommentsRepository extends CrudRepository<Comments,Long> {

    LinkedList<Comments> findByArticlesOrderByIdDesc(Articles articles);

    LinkedList<Comments> findByBody(String body);

    @Transactional
    void deleteById(Long id);
}
