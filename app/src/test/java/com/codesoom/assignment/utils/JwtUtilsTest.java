package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtUtilsTest {
    private static final String SECRET = "12345678901234567890123456789012";

    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9." +
            "ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";

    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9." +
            "ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaD0";


    private JwtUtils jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtils(SECRET);
    }

    @Test
    @DisplayName("JwtUtil.encode() accessToken값을 확인")
    void encode() {
        String token = jwtUtil.encode(1L);
        assertThat(token).isEqualTo(VALID_TOKEN);
    }

    @Test
    @DisplayName("존재하는 사용자의 VALID_TOKEN")
    void decodeWithValidToken() {
        Claims claims = jwtUtil.decode(VALID_TOKEN);

        // JavaScript같은 경우 number type으로만 이루어져있기 때문에 뒤에 L이 붙지 않는다.
        // 따라서 Long Type으로 얻기 위해 Long.class를 선언한다.
        assertThat(claims.get("userId", Long.class)).isEqualTo(1L);
    }

    @Test
    @DisplayName("존재하지 않는 사용자의 Token 값을 확인시 error") // SignatureException
    void decodeWithEmptyToken() {
        assertThatThrownBy(() -> jwtUtil.decode(null))
                .isInstanceOf(InvalidTokenException.class);

        assertThatThrownBy(() -> jwtUtil.decode(""))
                        .isInstanceOf(InvalidTokenException.class);

        assertThatThrownBy(() -> jwtUtil.decode("     "))
                        .isInstanceOf(InvalidTokenException.class);

        assertThatThrownBy(() -> jwtUtil.decode(INVALID_TOKEN))
                        .isInstanceOf(InvalidTokenException.class);
    }
}
