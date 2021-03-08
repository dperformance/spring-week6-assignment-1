package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.errors.LoginFailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
class SessionControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        given(authenticationService.login("test@gmail.com", "test"))
                .willReturn("a.b.c");

        given(authenticationService.login("badguy@gmail.com", "test"))
                .willThrow(new LoginFailException("badguy@gmail.com"));

        given(authenticationService.login("test@gmail.com", "badguy"))
                .willThrow(new LoginFailException("test@gmail.com"));
    }

    @Test
    @DisplayName("zzz")
    void loginWithRightEmailAndPassword() throws Exception {
        mvc.perform(
                post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@gmail.com\"," +
                                " \"password\":\"test\"}")
        )
        .andExpect(status().isCreated())
        .andExpect(content().string(containsString(".")));
    }

    @Test
    @DisplayName("Email 정보가 옳지 않다면 400 error (BAD_REQUEST)")
    void loginWithWrongEmailAndPassword() throws Exception {
        mvc.perform(
                post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"badguy@gmail.com\"," +
                                " \"password\":\"test\"}")
        )
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("Password 정보가 옳지 않다면 400 error (BAD_REQUEST)")
    void loginWithWrongPasswordAndPassword() throws Exception {
        mvc.perform(
                post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@gmail.com\"," +
                                " \"password\":\"badguy\"}")
        )
                .andExpect(status().isBadRequest());
    }

}