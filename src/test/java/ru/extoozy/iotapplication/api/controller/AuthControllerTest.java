package ru.extoozy.iotapplication.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@Sql(value = {
        "classpath:db/remove-test-data.sql",
        "classpath:db/insert-test-data.sql"
})
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void login() throws Exception {
        String testLoginRequestJson = """
                {
                    "username": "test_user",
                    "password": "test_password"
                }""";

        mockMvc.perform(post("/api/v1/auth/login")
                        .content(testLoginRequestJson)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access").exists())
                .andExpect(jsonPath("$.refresh").exists());
    }

    @Test
    void register() throws Exception {
        String testRegisterRequestJson = """
                {
                    "password": "password",
                    "username": "username"
                }""";

        mockMvc.perform(post("/api/v1/auth/register")
                        .content(testRegisterRequestJson)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.username").value("username"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void refresh() throws Exception {
        String testLoginRequestJson = """
                {
                    "username": "test_user",
                    "password": "test_password"
                }""";

        String authResult = mockMvc.perform(post("/api/v1/auth/login")
                        .content(testLoginRequestJson)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String prevRefresh = mapper.readTree(authResult).get("refresh").asText();
        String prevAccess = mapper.readTree(authResult).get("access").asText();

        String testRefreshJson = """
                {
                    "refreshToken": "%s"
                }""".formatted(prevRefresh);


        String refreshResult = mockMvc.perform(post("/api/v1/auth/refresh")
                        .content(testRefreshJson)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String newRefresh = mapper.readTree(refreshResult).get("refresh").asText();
        String newAccess = mapper.readTree(refreshResult).get("access").asText();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(prevRefresh).isEqualTo(newRefresh);
        softly.assertThat(newAccess).isNotEqualTo(prevAccess);

    }
}