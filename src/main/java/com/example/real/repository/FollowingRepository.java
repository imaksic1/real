package com.example.real.repository;

import com.example.real.model.Articles;
import com.example.real.model.Following;
import com.example.real.model.UserData;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.LinkedList;

public interface FollowingRepository extends CrudRepository<Following,Long> {

    LinkedList<Following> findByFollowingUser (UserData followingUser);

    LinkedList<Following> findByUserAndFollowingUser (UserData user, UserData followingUser);

    LinkedList<Following> findByUserOrderByIdDesc (UserData user);

    @Transactional
    void deleteByUserAndFollowingUser(UserData user, UserData followingUser);
}
