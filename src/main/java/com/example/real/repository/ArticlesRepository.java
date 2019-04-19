package com.example.real.repository;

import com.example.real.model.Articles;
import com.example.real.model.UserData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.LinkedList;

@Repository
public interface ArticlesRepository extends CrudRepository<Articles,Long> {
    LinkedList<Articles> findAll();

    LinkedList<Articles> findAllByOrderByIdDesc();

    LinkedList<Articles> findBySlug(String slug);

    LinkedList<Articles> findBySlugOrderByIdAsc(String slug);

    LinkedList<Articles> findByAuthorOrderByIdDesc(UserData Author);

    @Transactional
    void deleteById(Long id);
}
