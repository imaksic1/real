package com.example.real.service;

import com.example.real.dto.RegisterUser;
import com.example.real.dto.RegisterUserRequest;
import com.example.real.model.UserData;
import com.example.real.repository.UserLoginRegisterRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

@Service
public class LoginRegisterServise {

    @Autowired
    UserLoginRegisterRepository userLoginRegisterRepo;

    public boolean validUser(RegisterUser registerUser){
        if (registerUser.getEmail().length() == 0){
            return false;
        }
        if (registerUser.getPassword().length() < 8){
            return false;
        }
        if (registerUser.getUsername().length() < 1 || registerUser.getUsername().length() > 20){
            return false;
        }
        if (userLoginRegisterRepo.findByEmail(registerUser.getEmail()) != null){
            return false;
        }
        if (userLoginRegisterRepo.findByUsername(registerUser.getUsername()) != null){
            return false;
        }
        return true;
    }

    public JSONObject errRegister(RegisterUser registerUser){
        String errEmail = errEmail(registerUser.getEmail());
        String errPassword = errPassword(registerUser.getPassword());
        String errUsername = errUsername(registerUser.getUsername());
        String json1 = "";
        json1 += "{'errors':{";
        if (!errEmail.equals("")){
            json1 += errEmail;
            if (!errPassword.equals("") || !errUsername.equals("")){
                json1 += ",";
            }
        }
        if (!errPassword.equals("")){
            json1 += errPassword;
            if (!errUsername.equals("")){
                json1 += ",";
            }
        }
        if (!errUsername.equals("")){
            json1 += errUsername;
        }
        json1 += "}}";
        return new JSONObject(json1);
    }

    String errEmail (String email) {
        if (email.length() == 0) {
            return "'email':['can\\'t be blank']";
        }
        if (userLoginRegisterRepo.findByEmail(email) != null) {
            return "'email':['has already been taken']";
        }
        return "";
    }

    String errPassword (String password) {
        if (password.length() == 0) {
            return "'password':['can\\'t be blank']";
        }
        if (password.length() < 8) {
            return "'password':['is too short (minimum is 8 characters)']";
        }
        return "";
    }

    String errUsername (String username) {
        if (username.length() == 0) {
            return "'username':['can\\'t be blank','is too short (minimum is 1 character)']";
        }
        if (username.length() > 20) {
            return "'username':['is too long (maximum is 20 characters)']";
        }
        if (userLoginRegisterRepo.findByUsername(username) != null){
            return "'username':['has already been taken']";
        }
        return "";
    }

    public JSONObject errSignIn () {
        JSONObject json = new JSONObject();
        JSONObject jsonObj = new JSONObject();
        LinkedList<String> errString = new LinkedList();
        errString.add("is invalid");
        jsonObj.put("email or password", errString);
        json.put("errors", jsonObj);
        return json;
    }

    public JSONObject loginRegisterJson (String email) {
        UserData userData = new UserData();
        String json1;
        userData = userLoginRegisterRepo.findByEmail(email);
        if (userData == null){
            json1="{ERROR !  NO SUCH EMAIL !}";
            return new JSONObject (json1);
        }
        json1 = "{'user': {'id': " + userData.getId() + ",'email': " + userData.getEmail() + ",'createdAt': " + userData.getCreatedAt() + ",'updatedAt': " + userData.getUpdatedAt();
        json1 += ",'username': " + userData.getUsername() + ",'bio': " + userData.getBio() + ",'image': " + userData.getImage() + ",'token': " + userData.getToken() + "}}";
        return new JSONObject (json1);
    }

    public void saveToLoginRegisterRepository(RegisterUserRequest registerUserRequest){
        UserData userLoginRegister = new UserData();
        userLoginRegister.setEmail(registerUserRequest.getUser().getEmail());
        userLoginRegister.setPassword(registerUserRequest.getUser().getPassword());
        userLoginRegister.setUsername(registerUserRequest.getUser().getUsername());
        String createdAt = randomString(24);
        userLoginRegister.setCreatedAt(createdAt);
        userLoginRegister.setUpdatedAt(createdAt);
        userLoginRegister.setToken(randomString(250));
        userLoginRegisterRepo.save(userLoginRegister);
    }

    String randomString (Integer wide) {
        String output = "" ;
        for (int i=1; i<=wide; i++) {
            Integer rand = (int) (22 * Math.random());
            switch (rand) {
                case 0:
                    output += randNumber();
                    break;
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                    output += lowerCaseString();
                    break;
                default:
                    output += upperCaseString();
            }
        }
        return output;
    }

    String randNumber(){
        Integer randNumber = (int) (10 * Math.random());
        return Integer.toString(randNumber);
    }

    String lowerCaseString(){
        Integer randString = (int) (26 * Math.random());
        char randChar = 'a';
        randChar += randString;
        return String.valueOf(randChar);
    }

    String upperCaseString(){
        Integer randString = (int) (26 * Math.random());
        char randChar = 'A';
        randChar += randString;
        return String.valueOf(randChar);
    }

}
