package com.example.real.repository;

import com.example.real.model.UserData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoginRegisterRepository extends CrudRepository<UserData,Long> {

    UserData findByEmail(String email);

    UserData findByUsername(String username);
}
