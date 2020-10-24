package com.springboot.validation.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper = new ObjectMapper();

    @Test
    void 사용자_이름_null_에러() throws Exception {
        String user = mapper.writeValueAsString(User.builder()
                .username(null)
                .build());


        mvc.perform(post("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(user))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void 사용자_이름_null_메세지_에러() throws Exception {
        String user = mapper.writeValueAsString(User.builder()
                .username(null)
                .build());


        mvc.perform(post("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(user))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("사용자 이름을 입력해주세요"));
    }
}