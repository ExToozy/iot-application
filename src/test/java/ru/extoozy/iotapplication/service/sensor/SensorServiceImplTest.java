package ru.extoozy.iotapplication.service.sensor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.extoozy.iotapplication.api.exception.AlreadyExistsException;
import ru.extoozy.iotapplication.api.exception.NotFoundException;
import ru.extoozy.iotapplication.store.model.SensorEntity;
import ru.extoozy.iotapplication.store.repository.SensorRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SensorServiceImplTest {


    @Mock
    private SensorRepository sensorRepository;

    @InjectMocks
    private SensorServiceImpl sensorService;

    @Test
    void create_shouldCreateSensor_whenSensorNotExists() {
        SensorEntity sensor = SensorEntity.builder().name("test").build();

        when(sensorRepository.existsByName(sensor.getName())).thenReturn(false);
        when(sensorRepository.save(sensor)).thenReturn(sensor);

        sensorService.create(sensor);

        verify(sensorRepository).existsByName(sensor.getName());
        verify(sensorRepository).save(sensor);

    }

    @Test
    void create_shouldThrowException_whenSensorExists() {
        SensorEntity sensor = SensorEntity.builder().name("test").build();

        when(sensorRepository.existsByName(sensor.getName())).thenReturn(true);

        assertThatThrownBy(() -> sensorService.create(sensor))
                .isInstanceOf(AlreadyExistsException.class)
                .hasMessage("Sensor with name 'test' already exists");

        verify(sensorRepository).existsByName(sensor.getName());

    }

    @Test
    void getByName_shouldReturnSensor_whenSensorExists() {
        SensorEntity sensor = SensorEntity.builder().build();

        when(sensorRepository.findByName("mock")).thenReturn(Optional.of(sensor));

        sensorService.getByName("mock");

        verify(sensorRepository).findByName("mock");
    }

    @Test
    void getByName_shouldThrowException_whenSensorNotExists() {
        when(sensorRepository.findByName("sensor")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sensorService.getByName("sensor"))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Sensor with name 'sensor' not found");

        verify(sensorRepository).findByName("sensor");
    }
}