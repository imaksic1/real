package com.example.real.repository;

import com.example.real.model.Articles;
import com.example.real.model.Favorited;
import com.example.real.model.UserData;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.LinkedList;

public interface FavoritedRepository extends CrudRepository<Favorited,Long> {

    LinkedList<Favorited> findByUserOrderByIdDesc (UserData user);

    Favorited findByUserAndArticle (UserData user, Articles article);

    @Transactional
    void deleteByUserAndArticle(UserData user, Articles article);
}
