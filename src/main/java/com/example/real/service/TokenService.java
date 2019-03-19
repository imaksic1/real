package com.example.real.service;

import com.example.real.model.UserData;
import com.example.real.repository.UserLoginRegisterRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class TokenService {

    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;

    @Value("${security.jwt.token.expire-length:3600000}")
    private long validityInMilliseconds = 3600000; // 1h

    @Autowired
    UserLoginRegisterRepository userLoginRegisterRepository;

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

}
