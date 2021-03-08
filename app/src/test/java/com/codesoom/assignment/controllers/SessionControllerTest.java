package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
        given(authenticationService.login())
                .willReturn("a.b.c");
    }

    @Test
    @DisplayName("zzz")
    void login() throws Exception {
        mvc.perform(post("/session"))
        .andExpect(status().isCreated())
        .andExpect(content().string(containsString(".")));
    }

}