package com.example.real.service;

import com.example.real.dto.user.UserDataDTO;
import com.example.real.mapping.UserDataMapper;
import com.example.real.model.UserData;
import com.example.real.repository.UserLoginRegisterRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.real.specifications.UserDataSpecifications.findByEmail;


@Service
public class TokenService {

    @Autowired
    UserLoginRegisterRepository userLoginRegisterRepo;

    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;

    @Value("${security.jwt.token.expire-length:3600000}")
    private long validityInMilliseconds = 3600000; // 1h

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    @Autowired
    UserDataMapper userDataMapper;

    public void changeToken(UserData userData){
        //userData = userLoginRegisterRepo.findByEmail(userData.getEmail());
        userData = userLoginRegisterRepo.findOne(findByEmail(userData.getEmail()));
        userData.setToken(createToken(userData));
        userLoginRegisterRepo.save(userData);
    }

    public String createToken(UserData userdata) {
        Map<String, Object> mapClaims = new HashMap<>();
        mapClaims.put("id", userdata.getId());
        mapClaims.put("username", userdata.getUsername());

        Map<String, Object> mapHeader = new HashMap<>();
        mapHeader.put("typ", "JWT");

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()//
                .setClaims(mapClaims)//
                .setExpiration(validity)//
                .signWith(SignatureAlgorithm.HS256, secretKey)//
                .setHeader(mapHeader)
                .compact();
    }

    public String getTokenStringFromHeader (HttpHeaders headers){
        ArrayList<String> tokenList;
        String token;
        if (!headers.getValuesAsList("authorization").isEmpty()) {
            tokenList = (ArrayList) headers.getValuesAsList("authorization");
            token = tokenList.get(0);
            token = token.substring(6);
        }else{
            token = "";
        }
        return token;
    }

    public ResponseEntity getTokenFromHeaders(HttpHeaders headers){
        ArrayList<String> tokenList;
        tokenList = (ArrayList)headers.getValuesAsList("authorization");
        String token = tokenList.get(0);
        String exctractToken = token.substring(0,5);
        if (exctractToken.equals("Token")) {
            token = token.substring(6);
            return new ResponseEntity<>(getTokenUser(token), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public UserDataDTO getTokenUser(String token){
        UserData userData;
        userData = userLoginRegisterRepo.findByToken(token);
        return userDataMapper.UserDataToUserDataDTO(userData);
    }
}
