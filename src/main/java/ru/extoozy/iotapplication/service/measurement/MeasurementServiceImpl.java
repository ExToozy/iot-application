package ru.extoozy.iotapplication.service.measurement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.extoozy.iotapplication.service.sensor.SensorService;
import ru.extoozy.iotapplication.store.model.MeasurementEntity;
import ru.extoozy.iotapplication.store.model.SensorEntity;
import ru.extoozy.iotapplication.store.repository.MeasurementRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeasurementServiceImpl implements MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final SensorService sensorService;

    @Override
    @Transactional
    public MeasurementEntity create(MeasurementEntity entity) {
        SensorEntity sensor = sensorService.getByName(entity.getSensor().getName());
        entity.setSensor(sensor);
        return measurementRepository.save(entity);
    }

    @Override
    public List<MeasurementEntity> getAll() {
        return measurementRepository.findAll();
    }

    @Override
    public Long getRainyDaysCount() {
        return measurementRepository.countByRainingTrue();
    }
}
