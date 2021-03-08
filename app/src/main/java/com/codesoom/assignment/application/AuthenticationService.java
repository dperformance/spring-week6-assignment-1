package com.codesoom.assignment.application;

import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    // * HS256 = HMAC + SHA256을 합친 것.
    // Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    String secret1 = "dyson"; // 이렇게 하면 sha-256 error발생
    // 256 / 8 = 32 (32글자로 만들어야 함)
    private JwtUtils jwtUtils;

    public AuthenticationService(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }



    public String login() {
        // JwtUtils로 옮김.
        return jwtUtils.encode(1L);
    }

    public Long parseToken(String accessToken) {
        // 토큰이 null, blank일 경우 Exception 처리
        if (accessToken == null || accessToken.isBlank()) {
            throw new InvalidTokenException(accessToken);
        }

        try{
            Claims claims = jwtUtils.decode(accessToken);
            // <T> T get(String claimName, Class<T> requiredType);
            return claims.get("userId", Long.class);
        } catch (SignatureException e) {
            throw new InvalidTokenException(accessToken);
        }



    }
}
