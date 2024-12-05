package ru.extoozy.iotapplication;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.extoozy.iotapplication.api.dto.MeasurementDto;
import ru.extoozy.iotapplication.api.dto.SensorDto;

import java.util.Random;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void run() throws Exception {
        register();
        String access = login();
        addSensor(access);
        sendMeasurements(access);
        getMeasurements(access);
    }

    private void register() throws Exception {
        String testRegisterRequestJson = """
                {
                    "password": "password",
                    "username": "username"
                }""";

        mockMvc.perform(post("/api/v1/auth/register")
                        .content(testRegisterRequestJson)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andDo(print());


    }

    private String login() throws Exception {
        String testLoginRequestJson = """
                {
                    "username": "username",
                    "password": "password"
                }""";

        String loginResponse = mockMvc.perform(post("/api/v1/auth/login")
                        .content(testLoginRequestJson)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return mapper.readTree(loginResponse).get("access").asText();
    }

    private void addSensor(String access) throws Exception {
        String registerSensorJson = """
                {
                    "name": "sensor"
                }
                """;

        mockMvc.perform(post("/api/v1/sensors/register")
                        .content(registerSensorJson)
                        .header("Authorization", "Bearer " + access)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andDo(print());


    }

    private void sendMeasurements(String access) throws Exception {
        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            MeasurementDto measurement = MeasurementDto.builder()
                    .value(random.nextDouble(1, 100))
                    .raining(random.nextBoolean())
                    .sensor(new SensorDto(1L, "sensor"))
                    .build();
            mockMvc.perform(post("/api/v1/measurements/add")
                            .content(mapper.writeValueAsString(measurement))
                            .header("Authorization", "Bearer " + access)
                            .contentType("application/json"))
                    .andExpect(status().isOk());
        }

    }

    private void getMeasurements(String access) throws Exception {
        mockMvc.perform(get("/api/v1/measurements")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + access))
                .andExpect(jsonPath("$", hasSize(1000)));
    }
}
