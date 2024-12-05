package ru.extoozy.iotapplication.api.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest()
@Sql(value = {
        "classpath:db/remove-test-data.sql",
        "classpath:db/insert-test-data.sql"
})
@AutoConfigureMockMvc
@WithMockUser
class MeasurementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createMeasurement() throws Exception {

        String createMeasurementJson = """
                {
                    "value": 23.5,
                    "raining": true,
                    "sensor": {
                        "name": "test_sensor1"
                    }
                }
                """;

        mockMvc.perform(post("/api/v1/measurements/add")
                        .content(createMeasurementJson)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.value").value(23.5))
                .andExpect(jsonPath("$.raining").value(true))
                .andExpect(jsonPath("$.sensor.id").value(1))
                .andExpect(jsonPath("$.sensor.name").value("test_sensor1"));
    }

    @Test
    void getMeasurements() throws Exception {
        String expectedJson = """
                [
                  {
                    "id": 1,
                    "value": 10,
                    "raining": true,
                    "sensor": {
                      "id": 1,
                      "name": "test_sensor1"
                    }
                  },
                  {
                    "id": 2,
                    "value": 20,
                    "raining": false,
                    "sensor": {
                      "id": 1,
                      "name": "test_sensor1"
                    }
                  },
                  {
                    "id": 3,
                    "value": 30,
                    "raining": true,
                    "sensor": {
                      "id": 2,
                      "name": "test_sensor2"
                    }
                  }
                ]
                """;


        mockMvc.perform(get("/api/v1/measurements")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void getRainyDaysCount() throws Exception {
        mockMvc.perform(get("/api/v1/measurements/rainyDaysCount")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json("2"));
    }
}