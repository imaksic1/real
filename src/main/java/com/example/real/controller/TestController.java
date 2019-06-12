package com.example.real.controller;

import com.example.real.repository.UserLoginRegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;

@RestController
public class TestController {
    @Autowired
    UserLoginRegisterRepository userLoginRegisterRepo;

    @GetMapping("/api/test/allUsers")
    LinkedList<String> allUsers() {
        return userLoginRegisterRepo.findAllActiveUsers();
    }


}
