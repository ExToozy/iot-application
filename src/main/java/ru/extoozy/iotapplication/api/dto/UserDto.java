package ru.extoozy.iotapplication.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;

    @NotNull(message = "Username must not be null")
    @Length(min = 1, max = 255, message = "Username must be between 1 and 255 characters")
    private String username;

    @NotNull(message = "Password must not be null")
    @Length(min = 1, max = 255, message = "Password must be between 1 and 255 characters")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
}
