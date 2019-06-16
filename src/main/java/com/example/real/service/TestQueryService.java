package com.example.real.service;

import com.example.real.model.UserData;
import com.example.real.repository.UserLoginRegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.LinkedList;
import java.util.List;

import static com.example.real.specifications.TestCriteriaApiQuery.findByCriteriaApiQuery;

@Service
public class TestQueryService {
    @Autowired
    UserLoginRegisterRepository userLoginRegisterRepo;

    @PersistenceContext
    EntityManager entityManager;

    public List<String> jpqlTestQuery(String username, String email, String password, String token){
        String jpqlQuery = "SELECT u.username FROM UserData u";
        LinkedList<String> condition = new LinkedList();
        String con;
        if (username != null){
            con = " u.username LIKE '" + username + "%'";
            condition.addLast(con);
        }
        if (email != null){
            con = " u.email LIKE '" + email + "%'";
            condition.addLast(con);
        }
        if (password != null){
            con = " u.email LIKE '" + password + "%'";
            condition.addLast(con);
        }
        if (token != null){
            con = " u.token LIKE '" + token + "%'";
            condition.addLast(con);
        }
        if (!condition.isEmpty()){
            con =" WHERE " + condition.getFirst();
            for (int i = 1; i < condition.size(); i++){
                con = con + " OR " + condition.get(i);
            }
        }else{
            con="";
        }
        jpqlQuery = jpqlQuery + con;
        Query query = entityManager.
                createQuery(jpqlQuery);
        List<String> list = query.getResultList();
        return list;
    }
     public LinkedList<String> criteriaApiQuery (String username,String email,String password,String token){
         List<UserData> temp = userLoginRegisterRepo.findAll(findByCriteriaApiQuery(username, email, password, token));
         LinkedList<String> usernameList = new LinkedList();
         for (int i = 0; i < temp.size(); i++){
             usernameList.add(i,temp.get(i).getUsername());
         }
         return usernameList;
     }
}

