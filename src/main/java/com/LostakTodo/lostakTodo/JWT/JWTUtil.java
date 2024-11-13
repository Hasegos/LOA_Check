package com.LostakTodo.lostakTodo.JWT;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Service
@Component
// JWT 생성 / JWT 까주는 함수 포함된  클래스
public class JWTUtil {

    @Value("${jwt.secret-Key}")
    private static SecretKey secretKey; // jwt에 쓸 랜덤 키

    // JWT 만들어주는 함수
    public static String createToken(Authentication auth){
        // .claim(이름, 값) 으로 JWT에 데이터 추가 가능

        // 유저에대한 정보를 얻음 그 정보를 CustomUser를 통해 사용할 정보만 추출
        var user = (CustomUser) auth.getPrincipal();
        // 권한을 맵 자료로 받고 ,  를 기준으로 나눠서 등록
        var authorities = auth.getAuthorities().stream()
                .map(a -> a.getAuthority()).collect(Collectors.joining(","));

        String jwt = Jwts.builder()
                .claim("userEmail" , user.getUsername())
                .claim("password", user.getPassword())
                .claim("authorities",user.getAuthorities()) // 문자만 입력가능
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 10000)) // jwt 생존시간 ms으로 계산함
                .signWith(secretKey)
                .compact();
        return jwt;
    }
    
    // Claim 에 추가된 유저정보 즉, JWT 에있는 정보를 까주는 함수
    public static Claims extractToken(String token){
        Claims claims = Jwts.parser().verifyWith(secretKey).build()
                .parseEncryptedClaims(token).getPayload();

        return claims;
    }

}
