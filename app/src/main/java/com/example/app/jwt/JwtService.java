package com.example.app.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class JwtService {
    private static final String SECRET_KEY = "secret_key";
    public String generateToken(String username){
        return JWT
                .create()
                .withSubject("JWT TOKEN")
                .withIssuer(username)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + 15 * 60 * 1000 ))
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    public boolean validateJwtToken(String token){
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
        }catch (JWTVerificationException e){
            log.warn("JWT token is invalid");
            return false;
        }
        return true;
    }
    public String getJwtTokenFromRequestHeader(HttpServletRequest request){
        String bearer = request.getHeader("Authorization");
        String jwt = "";
        if(!bearer.isEmpty()  && !bearer.isBlank() && bearer.startsWith("Bearer")){
            jwt = bearer.substring(7);
        }
        return jwt;
    }

}
