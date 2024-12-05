package ru.extoozy.iotapplication.api.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@Sql({
        "classpath:db/remove-test-data.sql",
        "classpath:db/insert-test-data.sql"
})
@AutoConfigureMockMvc
@WithMockUser
class SensorControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    @DisplayName("Test create new sensor when sensor not exists")
    void registerSensor_shouldReturnNewSensor_whenSensorNotExists() throws Exception {

        String registerSensorJson = """
                {
                    "name":"new_sensor"
                }
                """;

        mockMvc.perform(post("/api/v1/sensors/register")
                        .content(registerSensorJson)
                        .contentType("application/json"))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("new_sensor"));

    }

    @Test
    @DisplayName("Test create sensor when sensor already exists")
    void registerSensor_shouldReturnError_whenSensorAlreadyExists() throws Exception {

        String registerSensorJson = """
                {
                    "name":"test_sensor1"
                }
                """;

        mockMvc.perform(post("/api/v1/sensors/register")
                        .content(registerSensorJson)
                        .contentType("application/json"))
                .andExpect(jsonPath("$.message").value(
                        "Sensor with name 'test_sensor1' already exists")
                );

    }
}