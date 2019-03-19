package com.example.real.service;

import com.example.real.dto.ErrorBody;
import com.example.real.dto.ErrorSignInBody;
import com.example.real.dto.ErrorSignInText;
import com.example.real.dto.ErrorText;
import com.example.real.dto.LoginUser;
import com.example.real.dto.RegisterUser;
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
public class LoginRegisterServise {

    @Autowired
    UserLoginRegisterRepository userLoginRegisterRepo;

    @Autowired
    TokenService tokenService;

    @Autowired
    UserDataMapper userDataMapper;

    public ResponseEntity<?> signInUser(LoginUser loginUser){
        UserData userData;
        ErrorSignInBody errorSignInBody = new ErrorSignInBody();
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
        return new ResponseEntity<>(userDataMapper.UserDataToUserDataDTO(userData) , HttpStatus.OK);
    }


        public ResponseEntity<?> validUser(RegisterUser registerUser){
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
            errorText.setError(errorBody);
            return new ResponseEntity<>(errorText, HttpStatus.UNPROCESSABLE_ENTITY);
        }else{
            saveToLoginRegisterRepository(registerUser);
            UserData userData = userLoginRegisterRepo.findByUsername(registerUser.getUsername());
            return new ResponseEntity<>(userDataMapper.UserDataToUserDataDTO(userData), HttpStatus.OK);
        }
    }

    LinkedList<String> errEmail (RegisterUser registerUser) {
        LinkedList<String> err = new LinkedList();
        if (registerUser.getEmail().length() == 0) {
            err.add("can't be blank");
        }
        if (userLoginRegisterRepo.findByEmail(registerUser.getEmail()) != null) {
            err.add("has already been taken");
        }
        return err;
    }

    LinkedList<String> errPassword (RegisterUser registerUser) {
        LinkedList<String> err = new LinkedList();
        if (registerUser.getPassword().length() == 0) {
            err.add("can't be blank");
            return err;
        }
        if (registerUser.getPassword().length() < 8) {
            err.add("is too short (minimum is 8 characters)");
        }
        return err;
    }

    LinkedList<String> errUsername (RegisterUser registerUser) {
        LinkedList<String> err = new LinkedList();
        if (registerUser.getUsername().length() == 0) {
            err.add("can't be blank','is too short (minimum is 1 character)");
        }
        if (registerUser.getUsername().length() > 20) {
            err.add("is too long (maximum is 20 characters)");
        }
        if (userLoginRegisterRepo.findByUsername(registerUser.getUsername()) != null){
            err.add("has already been taken");
        }
        return err;
    }

    public void saveToLoginRegisterRepository(RegisterUser registerUser){
        UserData userLoginRegister = new UserData();
        userLoginRegister.setEmail(registerUser.getEmail());
        userLoginRegister.setPassword(registerUser.getPassword());
        userLoginRegister.setUsername(registerUser.getUsername());
        Date createdAt = new Date();
        userLoginRegister.setCreatedAt(createdAt);
        userLoginRegister.setUpdatedAt(createdAt);
        userLoginRegister.setToken(tokenService.createToken(userLoginRegister));
        userLoginRegisterRepo.save(userLoginRegister);
    }

    String formatDateTime (Date inputDate){
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz").format(inputDate) ;
    }
}
