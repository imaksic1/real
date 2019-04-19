package com.example.real.service;

import com.example.real.dto.TagsSum;
import com.example.real.dto.error.ErrorBody;
import com.example.real.dto.error.ErrorText;
import com.example.real.dto.login.LoginUserResponse;
import com.example.real.dto.user.Settings;
import com.example.real.dto.user.SetttingsHeader;
import com.example.real.dto.TagsHeader;
import com.example.real.dto.user.UserDataDTO;
import com.example.real.dto.user.UserProfileResponse;
import com.example.real.dto.user.UserResponse;
import com.example.real.mapping.UserDataMapper;
import com.example.real.model.Following;
import com.example.real.model.UserData;
import com.example.real.repository.FollowingRepository;
import com.example.real.repository.TagsRepository;
import com.example.real.repository.UserLoginRegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;

@Service
public class StartUpService {

    @Autowired
    FollowingRepository followingRepository;

    @Autowired
    UserLoginRegisterRepository userLoginRegisterRepo;

    @Autowired
    TagsRepository tagsRepository;

    @Autowired
    UserDataMapper userDataMapper;

    @Autowired
    TokenService tokenService;

    public ResponseEntity returnUserRequest(HttpHeaders headers, String username){
        String token = tokenService.getTokenStringFromHeader (headers);
        UserData user = userLoginRegisterRepo.findByToken(token);
        UserData followingUser = userLoginRegisterRepo.findByUsername(username);
        followingUser.setFollowing(isFollowingUser(user,followingUser));
        UserResponse userResponse = userDataMapper.UserDataToUserResponse(followingUser);
        UserProfileResponse userProfileResponse = new UserProfileResponse();
        userProfileResponse.setProfile(userResponse);
        return new ResponseEntity<>(userProfileResponse, HttpStatus.OK);
    }

    public ResponseEntity deleteFollow(HttpHeaders headers, String username){
        String token = tokenService.getTokenStringFromHeader (headers);
        if (token == ""){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        UserData user = userLoginRegisterRepo.findByToken(token);
        UserData followingUser = userLoginRegisterRepo.findByUsername(username);
        followingRepository.deleteByUserAndFollowingUser(user,followingUser);
        followingUser.setFollowing(isFollowingUser(user,followingUser));
        return followResponse(user, followingUser);
    }

    public ResponseEntity addFollow(HttpHeaders headers, String username){
        String token = tokenService.getTokenStringFromHeader (headers);
        if (token == ""){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        UserData user = userLoginRegisterRepo.findByToken(token);
        UserData followingUser = userLoginRegisterRepo.findByUsername(username);
        Following following = new Following();
        following.setUser(user);
        following.setFollowingUser(followingUser);
        followingRepository.save(following);
        return followResponse(user,followingUser);
    }

    private boolean isFollowingUser(UserData user, UserData followingUser){
        if (user == null || followingUser == null){
            return false;
        }
        LinkedList<Following> followings = followingRepository.findByUserAndFollowingUser(user, followingUser);
        if (followings.isEmpty()){
            return false;
        }else{
            return true;
        }
    }

    private ResponseEntity followResponse (UserData user, UserData followingUser){
        followingUser.setFollowing(isFollowingUser(user, followingUser));
        UserResponse userResponse = userDataMapper.UserDataToUserResponse(followingUser);
        UserProfileResponse userProfileResponse = new UserProfileResponse();
        userProfileResponse.setProfile(userResponse);
        return new ResponseEntity<>(userProfileResponse, HttpStatus.OK);
    }

    public ResponseEntity examineSettings (HttpHeaders headers, SetttingsHeader settingsHeader){
        String token = tokenService.getTokenStringFromHeader (headers);
        UserData userData;
        userData = userLoginRegisterRepo.findByToken(token);
        return putSettingsData(settingsHeader,userData);
    }

    private ResponseEntity putSettingsData(SetttingsHeader settingsHeader,UserData userData){
        Settings settings = new Settings();
        settings = settingsHeader.getUser();
        ErrorText errorText = new ErrorText();
        ErrorBody errorBody = new ErrorBody();
        boolean hasError = false;
        LinkedList err;
        err = errorEmail(settings,userData);
        if (!err.isEmpty()){
            errorBody.setEmail(err);
            hasError = true;
        }
        err = errorPassword(settings);
        if (!err.isEmpty()){
            errorBody.setPassword(err);
            hasError = true;
        }
        err = errorUsername(settings,userData);
        if (!err.isEmpty()){
            errorBody.setUsername(err);
            hasError = true;
        }
        if (hasError){
            errorText.setErrors(errorBody);
            return new ResponseEntity<>(errorText, HttpStatus.UNPROCESSABLE_ENTITY);
        }else{
            saveSettingsToLoginRegisterRepository(settings,userData);
            UserData userDataOld = userLoginRegisterRepo.findByUsername(settings.getUsername());
            UserDataDTO userDataDTO = userDataMapper.UserDataToUserDataDTO(userDataOld);
            LoginUserResponse loginUserResponse = new LoginUserResponse();
            loginUserResponse.setUser(userDataDTO);
            return new ResponseEntity<>(loginUserResponse, HttpStatus.OK);
        }
    }

    private void saveSettingsToLoginRegisterRepository(Settings settings,UserData userData){
        if (!(settings.getPassword() == null || settings.getPassword().length() == 0)){
            userData.setPassword(settings.getPassword());
        }
        if (!(settings.getUsername() == null || settings.getUsername().length() == 0)){
            userData.setUsername(settings.getUsername());
        }
        userData.setEmail(settings.getEmail());
        userData.setBio(settings.getBio());
        userData.setImage(settings.getImage());
        userLoginRegisterRepo.save(userData);
    }

    LinkedList<String> errorEmail (Settings settings,UserData userData) {
        LinkedList<String> err = new LinkedList();
        if (settings.getEmail() == null || settings.getEmail().length() == 0) {
            err.add("can't be blank");
            return err;
        }
        if (userLoginRegisterRepo.findByEmail(settings.getEmail()) != null && !settings.getEmail().equals(userData.getEmail())) {
            err.add("has already been taken");
            return err;
        }
        return err;
    }

    private LinkedList<String> errorPassword (Settings settings) {
        LinkedList<String> err = new LinkedList();
        if (settings.getPassword() == null || settings.getPassword().length() == 0) {
            return err;
        }
        if (settings.getPassword().length() < 8) {
            err.add("is too short (minimum is 8 characters)");
        }
        return err;
    }

    private LinkedList<String> errorUsername (Settings settings,UserData userData) {
        LinkedList<String> err = new LinkedList();
        if (settings.getUsername() == null || settings.getUsername().length() == 0) {
            err.add("can't be blank, is too short (minimum is 1 character)");
        }
        if (settings.getUsername().length() > 20) {
            err.add("is too long (maximum is 20 characters)");
        }
        if (userLoginRegisterRepo.findByUsername(settings.getUsername()) != null && !settings.getUsername().equals(userData.getUsername())){
            err.add("has already been taken");
        }
        return err;
    }

    public TagsHeader fillTags(){
        LinkedList<String> tagsSum = tagsRepository.findTagsSum();
        LinkedList<String> tags = new LinkedList<>();
        for (int i = 0; i < tagsSum.size() && i < 20; i++){
            tags.add(tagsSum.get(i));
        }
        System.out.println(tags);
        TagsHeader tagsHeader = new TagsHeader();
        tagsHeader.setTags(tags);
        return tagsHeader;
    }
}
