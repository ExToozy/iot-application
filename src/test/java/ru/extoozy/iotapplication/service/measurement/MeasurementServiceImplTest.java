package ru.extoozy.iotapplication.service.measurement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.extoozy.iotapplication.service.sensor.SensorService;
import ru.extoozy.iotapplication.store.model.MeasurementEntity;
import ru.extoozy.iotapplication.store.model.SensorEntity;
import ru.extoozy.iotapplication.store.repository.MeasurementRepository;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MeasurementServiceImplTest {

    @InjectMocks
    private MeasurementServiceImpl measurementService;

    @Mock
    private MeasurementRepository measurementRepository;

    @Mock
    private SensorService sensorService;

    @Test
    void create() {
        SensorEntity sensor = SensorEntity.builder()
                .id(1L)
                .name("sensor")
                .build();

        MeasurementEntity measurement = MeasurementEntity.builder()
                .id(1L)
                .value(20D)
                .raining(true)
                .sensor(sensor)
                .build();


        when(sensorService.getByName(sensor.getName())).thenReturn(sensor);
        when(measurementRepository.save(measurement)).thenReturn(measurement);

        measurementService.create(measurement);

        verify(sensorService).getByName(sensor.getName());
        verify(measurementRepository).save(measurement);
    }

    @Test
    void getAll() {
        SensorEntity sensor = SensorEntity.builder()
                .id(1L)
                .name("sensor")
                .build();

        MeasurementEntity measurement = MeasurementEntity.builder()
                .id(1L)
                .value(20D)
                .raining(true)
                .sensor(sensor)
                .build();


        when(measurementRepository.findAll()).thenReturn(new ArrayList<>(List.of(measurement)));

        measurementService.getAll();

        verify(measurementRepository).findAll();
    }

    @Test
    void getRainyDaysCount() {
        when(measurementRepository.countByRainingTrue()).thenReturn(1L);

        measurementService.getRainyDaysCount();

        verify(measurementRepository).countByRainingTrue();

    }
}