package com.example.real.service;

import com.example.real.dto.error.ErrorBody;
import com.example.real.dto.error.ErrorSignInText;
import com.example.real.dto.error.ErrorText;
import com.example.real.dto.login.LoginUser;
import com.example.real.dto.login.LoginUserRequest;
import com.example.real.dto.login.LoginUserResponse;
import com.example.real.dto.login.RegisterUser;
import com.example.real.dto.login.RegisterUserRequest;
import com.example.real.dto.user.UserDataDTO;
import com.example.real.mapping.UserDataMapper;
import com.example.real.model.UserData;
import com.example.real.repository.UserLoginRegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

@Service
public class LoginRegisterService {

    @Autowired
    UserLoginRegisterRepository userLoginRegisterRepo;

    @Autowired
    TokenService tokenService;

    @Autowired
    UserDataMapper userDataMapper;

    public ResponseEntity signInUser(LoginUserRequest loginUserRequest){
        LoginUser loginUser = new LoginUser();
        loginUser = loginUserRequest.getUser();
        UserData userData = new UserData();
        ErrorSignInText errorSignInText = new ErrorSignInText();
        HashMap<String,LinkedList<String>> hashMap = new HashMap<>();
        LinkedList<String> text = new LinkedList<>();
        text.add("is invalid");
        hashMap.put("email or password",text);
        errorSignInText.setErrors(hashMap);

        if (loginUser.getEmail().equals("")  || loginUser.getPassword().equals("") ) {
            return new ResponseEntity<>(errorSignInText, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        userData = userLoginRegisterRepo.findByEmail(loginUser.getEmail());
        if (userData == null){
            return new ResponseEntity<>(errorSignInText, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (!userData.getPassword().equals(loginUser.getPassword())){
            return new ResponseEntity<>(errorSignInText, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        tokenService.changeToken(userData);
        userData = userLoginRegisterRepo.findByEmail(loginUser.getEmail());
        UserDataDTO userDataDTO = userDataMapper.UserDataToUserDataDTO(userData);
        LoginUserResponse loginUserResponse = new LoginUserResponse();
        loginUserResponse.setUser(userDataDTO);
        return new ResponseEntity<>(loginUserResponse , HttpStatus.OK);
    }

    public ResponseEntity validUser(RegisterUserRequest registerUserRequest){
        RegisterUser registerUser = new RegisterUser();
        registerUser = registerUserRequest.getUser();
        ErrorText errorText = new ErrorText();
        ErrorBody errorBody = new ErrorBody();
        boolean hasError = false;
        LinkedList err;
        err = errEmail(registerUser);
        if (!err.isEmpty()){
            errorBody.setEmail(err);
            hasError = true;
        }
        err = errPassword(registerUser);
        if (!err.isEmpty()){
            errorBody.setPassword(err);
            hasError = true;
        }
        err = errUsername(registerUser);
        if (!err.isEmpty()){
            errorBody.setUsername(err);
            hasError = true;
        }
        if (hasError){
            errorText.setErrors(errorBody);
            return new ResponseEntity<>(errorText, HttpStatus.UNPROCESSABLE_ENTITY);
        }else{
            saveToLoginRegisterRepository(registerUser);
            UserData userData = userLoginRegisterRepo.findByUsername(registerUser.getUsername());
            UserDataDTO userDataDTO = userDataMapper.UserDataToUserDataDTO(userData);
            LoginUserResponse loginUserResponse = new LoginUserResponse();
            loginUserResponse.setUser(userDataDTO);
            return new ResponseEntity<>(loginUserResponse, HttpStatus.OK);
        }
    }

    private LinkedList<String> errEmail (RegisterUser registerUser) {
        LinkedList<String> err = new LinkedList();
        if (registerUser.getEmail() == null || registerUser.getEmail().length() == 0) {
            err.add("can't be blank");
        }
        if (userLoginRegisterRepo.findByEmail(registerUser.getEmail()) != null) {
            err.add("has already been taken");
        }
        return err;
    }

    private LinkedList<String> errPassword (RegisterUser registerUser) {
        LinkedList<String> err = new LinkedList();
        if (registerUser.getPassword() == null || registerUser.getPassword().length() == 0) {
            err.add("can't be blank");
            return err;
        }
        if (registerUser.getPassword().length() < 8) {
            err.add("is too short (minimum is 8 characters)");
        }
        return err;
    }

    private LinkedList<String> errUsername (RegisterUser registerUser) {
        LinkedList<String> err = new LinkedList();
        if (registerUser.getUsername() == null || registerUser.getUsername().length() == 0) {
            err.add("can't be blank, is too short (minimum is 1 character)");
        }
        if (registerUser.getUsername().length() > 20) {
            err.add("is too long (maximum is 20 characters)");
        }
        if (userLoginRegisterRepo.findByUsername(registerUser.getUsername()) != null){
            err.add("has already been taken");
        }
        return err;
    }

    private void saveToLoginRegisterRepository(RegisterUser registerUser){
        UserData userLoginRegister = new UserData();
        userLoginRegister.setEmail(registerUser.getEmail());
        userLoginRegister.setPassword(registerUser.getPassword());
        userLoginRegister.setUsername(registerUser.getUsername());
        Date createdAt = new Date();
        userLoginRegister.setCreatedAt(createdAt);
        userLoginRegister.setUpdatedAt(createdAt);
        userLoginRegisterRepo.save(userLoginRegister);
        userLoginRegister = userLoginRegisterRepo.findByEmail(registerUser.getEmail());
        userLoginRegister.setToken(tokenService.createToken(userLoginRegister));
        userLoginRegisterRepo.save(userLoginRegister);
    }
}
