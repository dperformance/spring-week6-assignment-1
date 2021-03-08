package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.LoginFailException;
import com.codesoom.assignment.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AuthenticationServiceTest {

    private static final String SECRET = "12345678901234567890123456789012";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaD0";

    private AuthenticationService authenticationService;

    private UserRepository userRepository = mock(UserRepository.class);

    @BeforeEach
    void setUp() {
        JwtUtils jwtUtil = new JwtUtils(SECRET);

        authenticationService = new AuthenticationService(jwtUtil, userRepository);

        User user = User.builder()
                .password("test")
                .build();

        given(userRepository.findByEmail("test@gmail.com"))
                .willReturn(Optional.of(user));
    }

    @Test
    void loginWithRightEmailAndPassword() {
        String accessToken = authenticationService.login(
                "test@gmail.com", "test");

        // sout은 log에 출력된 것을 찾아야 하기 때문에 accessToken값을 보고자 할 때는
//        System.out.println("accessToken : " + accessToken);

        // 일부러 test가 실패하도록 한다. ex) assetThat(accessToken).contains(".aaas");
        assertThat(accessToken).isEqualTo(VALID_TOKEN);

        verify(userRepository).findByEmail("test@gmail.com");
    }

    @Test
    void loginWithWrongEmail() {
        assertThatThrownBy(
                () -> authenticationService.login("badguy@gmail.com", "test")
        ).isInstanceOf(LoginFailException.class);

        verify(userRepository).findByEmail("badguy@gmail.com");
    }

    @Test
    void loginWithWrongPassword() {
        assertThatThrownBy(
                () -> authenticationService.login("test@gmail.com", "badguy")
        ).isInstanceOf(LoginFailException.class);

        verify(userRepository).findByEmail("test@gmail.com");
    }

    @Test
    @DisplayName("토근을 확인하여 사용자를 반환합니다.")
    void parseTokenWithValidToken() {
        Long userId = authenticationService.parseToken(VALID_TOKEN);
        assertThat(userId).isEqualTo(1L);
    }

    @Test
    @DisplayName("비정상적인 토큰일때 error를 발생")
    void parseTokenWithInValidToken() {
        // Invalid token으로 id를 얻으려 했기 때문에 Exception이 일어난다.
//        Long userId = authenticationService.parseToken(INVALID_TOKEN);
        assertThatThrownBy(
                () -> authenticationService.parseToken(INVALID_TOKEN)
        ).isInstanceOf(InvalidTokenException.class);
    }

    @Test
    @DisplayName("토큰이 비어있거나 null일경우 error를 발생")
    void parseTokenWithEmptyToken() {
        assertThatThrownBy(
                () -> authenticationService.parseToken(null)
        ).isInstanceOf(InvalidTokenException.class);

        assertThatThrownBy(
                () -> authenticationService.parseToken("")
        ).isInstanceOf(InvalidTokenException.class);

        assertThatThrownBy(
                () -> authenticationService.parseToken("         ")
        ).isInstanceOf(InvalidTokenException.class);
    }
}
