package com.example.real.specifications;

import com.example.real.model.UserData;
import com.example.real.model.UserData_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Collection;


public class TestCriteriaApiQuery {

    public static Specification<UserData> findByCriteriaApiQuery(String username, String email, String password, String token) {
        return (root, query, cb) -> {
            Collection<Predicate> predicates = new ArrayList<>();
            Predicate predicateUsername = null;
            Predicate predicateEmail = null;
            Predicate predicatePassword = null;
            Predicate predicateToken = null;
            if (username != null) {
                predicateUsername = cb.like(root.get(UserData_.username), username+"%");
                predicates.add(predicateUsername);
            }
            if (email != null){
                predicateEmail = cb.like(root.get(UserData_.email), email+"%");
                predicates.add(predicateEmail);
            }
            if (password != null){
                predicatePassword = cb.like(root.get(UserData_.password), password+"%");
                predicates.add(predicatePassword);
            }
            if (token != null){
                predicateToken = cb.like(root.get(UserData_.token), token+"%");
                predicates.add(predicateToken);
            }
            //query.select(root.get("username"));
            if (predicates.size() > 0 ) {
                return cb.or(predicates.toArray(new Predicate[predicates.size()]));
            }else{
                return null;
            }
        };
    }
}
