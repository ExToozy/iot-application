package ru.extoozy.iotapplication.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeasurementDto {

    private Long id;

    @NotNull(message = "Value must not be null")
    @Positive(message = "Value must be greater than zero")
    private Double value;

    @NotNull(message = "Raining must not be null")
    private Boolean raining;

    @NotNull(message = "Sensor must not be null")
    @Valid
    private SensorDto sensor;
}
