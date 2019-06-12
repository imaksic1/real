package com.example.real.repository;

import com.example.real.model.UserData;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.LinkedList;

@Repository
public interface UserLoginRegisterRepository extends CrudRepository<UserData,Long>, JpaSpecificationExecutor<UserData> {

    UserData findByEmail(String email);

    UserData findByUsername(String username);

    UserData findByToken (String token);

    @Transactional
    void deleteByUsername(String username);

    @Query("SELECT u.username FROM UserData u")
    LinkedList<String> findAllActiveUsers();
}
