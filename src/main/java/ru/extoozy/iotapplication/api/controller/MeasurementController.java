package ru.extoozy.iotapplication.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.extoozy.iotapplication.api.dto.MeasurementDto;
import ru.extoozy.iotapplication.api.mapper.MeasurementMapper;
import ru.extoozy.iotapplication.service.measurement.MeasurementService;
import ru.extoozy.iotapplication.store.model.MeasurementEntity;

import java.util.List;

@RestController
@RequestMapping("/api/v1/measurements")
@RequiredArgsConstructor
public class MeasurementController {

    private final MeasurementService measurementService;

    private final MeasurementMapper mapper;

    @PostMapping("/add")
    public MeasurementDto createMeasurement(@RequestBody @Validated MeasurementDto dto) {
        MeasurementEntity entity = mapper.toEntity(dto);
        MeasurementEntity createdEntity = measurementService.create(entity);
        return mapper.toDto(createdEntity);
    }

    @GetMapping
    public List<MeasurementDto> getMeasurements() {
        List<MeasurementEntity> entities = measurementService.getAll();
        return mapper.toDto(entities);
    }

    @GetMapping("/rainyDaysCount")
    public Long getRainyDaysCount() {
        return measurementService.getRainyDaysCount();
    }
}
