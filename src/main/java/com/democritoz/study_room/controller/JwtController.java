package com.democritoz.study_room.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController("/jwt")
@Slf4j
public class JwtController {


    private String SECRET_KEY =  "TODO_CHANGE_test_secret_key";

    private String getSecretKey() {
        return getSecretKey(SECRET_KEY);
    }

    private String getSecretKey(String secret) {
        return Base64.getEncoder().encodeToString(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Data
    public static class Input {

        private String jwt;

    }

    @PostMapping("/validateToken")
    public ResponseEntity<Map<String, Object>> validateToken(@RequestBody Input input) {

        Claims claims = Jwts.parser().setSigningKey(getSecretKey()).parseClaimsJws(input.getJwt()).getBody();

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("claims", claims);
        return ResponseEntity.ok(responseMap);
    }

    @GetMapping("/createToken")
    public ResponseEntity<Map<String, Object>> createToken() {


        LocalDateTime createdDateTime = LocalDateTime.now();
        log.debug("createdDateTime #1=[{}]", createdDateTime);

        LocalDateTime expireDateTime = createdDateTime.plusMonths(1);

        log.debug("createdDateTime #2=[{}]", createdDateTime);
        log.debug("expireDateTime #1=[{}]", expireDateTime);

        log.debug("secretKey=[{}]", getSecretKey());

        final String generatedJWT = Jwts.builder()
                .claim("userName", "Monad")
                .claim("email", "democritoz.monad@gmail.com")
                .setSubject("test_subject")
                .setIssuedAt(new Date(createdDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()))
                .setExpiration(new Date(expireDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()))
                .signWith(SignatureAlgorithm.HS256, getSecretKey())
                .compact();

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("JWT", generatedJWT);
        return ResponseEntity.ok(responseMap);
    }

}
