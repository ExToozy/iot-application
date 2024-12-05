package ru.extoozy.iotapplication.api.security.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Data
@ConfigurationProperties("security.jwt")
public class JwtProperties {
    private String secret;
    private Duration access;
    private Duration refresh;
}
