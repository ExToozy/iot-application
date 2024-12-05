package ru.extoozy.iotapplication.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.extoozy.iotapplication.api.dto.SensorDto;
import ru.extoozy.iotapplication.api.mapper.SensorMapper;
import ru.extoozy.iotapplication.service.sensor.SensorService;
import ru.extoozy.iotapplication.store.model.SensorEntity;

@RestController
@RequestMapping("/api/v1/sensors")
@RequiredArgsConstructor
public class SensorController {

    private final SensorService sensorService;
    private final SensorMapper mapper;

    @PostMapping("/register")
    public SensorDto registerSensor(@RequestBody @Validated SensorDto dto) {
        SensorEntity entity = mapper.toEntity(dto);
        SensorEntity savedEntity = sensorService.create(entity);
        return mapper.toDto(savedEntity);
    }


}
