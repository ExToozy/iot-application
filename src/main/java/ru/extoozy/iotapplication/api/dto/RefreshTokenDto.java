package ru.extoozy.iotapplication.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RefreshTokenDto {

    @NotNull(message = "Refresh token must not be null")
    private String refreshToken;
}
