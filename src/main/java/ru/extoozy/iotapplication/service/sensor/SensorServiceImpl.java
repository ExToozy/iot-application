package ru.extoozy.iotapplication.service.sensor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.extoozy.iotapplication.api.exception.AlreadyExistsException;
import ru.extoozy.iotapplication.api.exception.NotFoundException;
import ru.extoozy.iotapplication.store.model.SensorEntity;
import ru.extoozy.iotapplication.store.repository.SensorRepository;

@Service
@RequiredArgsConstructor
public class SensorServiceImpl implements SensorService {

    private final SensorRepository sensorRepository;

    @Override
    @Transactional
    public SensorEntity create(SensorEntity entity) {
        if (sensorRepository.existsByName(entity.getName())) {
            throw new AlreadyExistsException(
                    "Sensor with name '%s' already exists".formatted(entity.getName())
            );
        }
        return sensorRepository.save(entity);
    }

    @Override
    public SensorEntity getByName(String name) {
        return sensorRepository
                .findByName(name)
                .orElseThrow(() -> new NotFoundException(
                        "Sensor with name '%s' not found".formatted(name)
                ));
    }
}
