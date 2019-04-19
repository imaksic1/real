package com.example.real.controller;


import com.example.real.dto.login.LoginUser;
import com.example.real.dto.login.LoginUserRequest;
import com.example.real.dto.login.RegisterUserRequest;
import com.example.real.repository.UserLoginRegisterRepository;
import com.example.real.service.LoginRegisterService;
import com.example.real.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LoginController {

    @Autowired
    LoginRegisterService logRegService;

    @Autowired
    TokenService tokenService;

    @PostMapping("/api/users/login")
    public ResponseEntity loginUserRequest(@RequestBody LoginUserRequest loginUserRequest) {
        return logRegService.signInUser(loginUserRequest);
    }

    @PostMapping("/api/users")
    public ResponseEntity registerUserRequest(@RequestBody RegisterUserRequest registerUserRequest){
            return logRegService.validUser(registerUserRequest);
        }

    @GetMapping("/api/user")
    public ResponseEntity regTokenUserRequest(@RequestHeader HttpHeaders headers){
        return tokenService.getTokenFromHeaders(headers);
    }
}
