package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.LoginFailException;
import com.codesoom.assignment.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
    // * HS256 = HMAC + SHA256을 합친 것.
    // Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    String secret1 = "dyson"; // 이렇게 하면 sha-256 error발생
    // 256 / 8 = 32 (32글자로 만들어야 함)
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public AuthenticationService(JwtUtils jwtUtils,
                                 UserRepository userRepository) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }



    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new LoginFailException(email));

        if (!user.authenticate(password)) {
            throw new LoginFailException(email);
        }
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
