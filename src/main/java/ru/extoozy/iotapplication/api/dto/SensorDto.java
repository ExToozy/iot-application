package ru.extoozy.iotapplication.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorDto {

    private Long id;

    @NotNull(message = "Name must not be null")
    @Length(
            min = 1, max = 255,
            message = "Sensor name must be between 1 and 255 characters"
    )
    private String name;
}
