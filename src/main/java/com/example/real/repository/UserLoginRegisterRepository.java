package com.example.real.repository;

import com.example.real.model.UserData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface UserLoginRegisterRepository extends CrudRepository<UserData,Long> {

    UserData findByEmail(String email);

    UserData findByUsername(String username);

    UserData findByToken (String token);

    @Transactional
    void deleteByUsername(String username);
}
