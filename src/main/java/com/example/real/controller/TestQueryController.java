package com.example.real.controller;

import com.example.real.model.UserData;
import com.example.real.repository.UserLoginRegisterRepository;
import com.example.real.service.TestQueryService;
import com.example.real.specifications.TestCriteriaApiQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

import static com.example.real.specifications.TestCriteriaApiQuery.findByCriteriaApiQuery;

@RestController
public class TestQueryController {
    @Autowired
    UserLoginRegisterRepository userLoginRegisterRepo;

    @Autowired
    TestQueryService testQueryService;

    @GetMapping("/api/test/allUsers")
    LinkedList<String> allUsers() {
        return userLoginRegisterRepo.findAllActiveUsers();
    }

    @GetMapping("/api/test/likeUsers/{username}")
    LinkedList<String> likeUsers(@PathVariable("username") String username) {
        return userLoginRegisterRepo.findLikeUsername(username);
    }

    @GetMapping("/api/jpqlQuery")
    public List<String> getJpqlQuery(@RequestParam(value = "username", required=false) String username,
                                     @RequestParam(value = "email", required=false) String email,
                                     @RequestParam(value = "password", required=false) String password,
                                     @RequestParam(value = "token", required=false) String token) {
        return testQueryService.jpqlTestQuery(username, email, password, token);
    }

    @GetMapping("/api/CriteriaApiQuery")
    public LinkedList<String> getCriteriaApiQuery(@RequestParam(value = "username", required=false) String username,
                                              @RequestParam(value = "email", required=false) String email,
                                              @RequestParam(value = "password", required=false) String password,
                                              @RequestParam(value = "token", required=false) String token) {
        return testQueryService.criteriaApiQuery(username, email, password, token);
    }
}
