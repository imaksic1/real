package com.example.real.controller;

import com.example.real.dto.LoginUserRequest;
import com.example.real.dto.RegisterUser;
import com.example.real.dto.RegisterUserRequest;
import com.example.real.model.UserData;
import com.example.real.repository.UserLoginRegisterRepository;
import com.example.real.service.LoginRegisterServise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> loginUserRequest111(@RequestBody LoginUserRequest loginUserRequest) {
        UserData userData;
        if (loginUserRequest.getUser().getEmail().equals("")  || loginUserRequest.getUser().getPassword().equals("") ) {
            return new ResponseEntity<>(logRegService.errSignIn().toString(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        userData = userLoginRegisterRepo.findByEmail(loginUserRequest.getUser().getEmail());
        if (userData == null){
            return new ResponseEntity<>(logRegService.errSignIn().toString(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (!userData.getPassword().equals(loginUserRequest.getUser().getPassword())){
            return new ResponseEntity<>(logRegService.errSignIn().toString(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(logRegService.loginRegisterJson(loginUserRequest.getUser().getEmail()).toString() , HttpStatus.OK);
    }

    @PostMapping("/api/users")
    public ResponseEntity<?> registerUserRequest(@RequestBody RegisterUserRequest registerUserRequest) {
        RegisterUser registerUser = new RegisterUser();
        registerUser = registerUserRequest.getUser();
        if (logRegService.validUser(registerUser)) {
            logRegService.saveToLoginRegisterRepository(registerUserRequest);
            return new ResponseEntity<>(logRegService.loginRegisterJson(registerUserRequest.getUser().getEmail()).toString(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(logRegService.errRegister(registerUser).toString(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

}
