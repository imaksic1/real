package com.example.real.controller;


import com.example.real.dto.LoginUser;
import com.example.real.dto.LoginUserRequest;
import com.example.real.dto.RegisterUser;
import com.example.real.dto.RegisterUserRequest;
import com.example.real.repository.UserLoginRegisterRepository;
import com.example.real.service.LoginRegisterServise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    LoginRegisterServise logRegService;

    @Autowired
    UserLoginRegisterRepository userLoginRegisterRepo;

    @PostMapping("/api/users/login")
    public ResponseEntity<?> loginUserRequest(@RequestBody LoginUserRequest loginUserRequest) {
        LoginUser loginUser = new LoginUser();
        loginUser = loginUserRequest.getUser();
        return logRegService.signInUser(loginUser);
    }

    @PostMapping("/api/users")
    public ResponseEntity<?> registerUserRequest(@RequestBody RegisterUserRequest registerUserRequest) {
        RegisterUser registerUser = new RegisterUser();
        registerUser = registerUserRequest.getUser();
        return logRegService.validUser(registerUser);
    }
}
